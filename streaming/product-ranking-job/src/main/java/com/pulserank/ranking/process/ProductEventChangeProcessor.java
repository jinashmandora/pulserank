package com.pulserank.ranking.process;

import com.pulserank.ranking.utils.WeightCalculator;
import com.pulserank.schema.event.ProductChangeEvent;
import com.pulserank.schema.event.ProductDetails;
import com.pulserank.schema.event.ProductIntersectionEvent;
import com.pulserank.schema.event.ProductScoreEvent;
import org.apache.flink.api.common.functions.OpenContext;
import org.apache.flink.api.common.state.MapState;
import org.apache.flink.api.common.state.MapStateDescriptor;
import org.apache.flink.api.common.state.ValueState;
import org.apache.flink.api.common.state.ValueStateDescriptor;
import org.apache.flink.api.common.typeinfo.Types;
import org.apache.flink.formats.avro.typeutils.AvroTypeInfo;
import org.apache.flink.streaming.api.functions.co.KeyedCoProcessFunction;
import org.apache.flink.util.Collector;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;
import java.time.Instant;

public class ProductEventChangeProcessor extends KeyedCoProcessFunction<
        Long,
        ProductIntersectionEvent,
        ProductChangeEvent,
        ProductScoreEvent> {

    private static final Logger LOG = LoggerFactory.getLogger(ProductEventChangeProcessor.class);

    private static final long WINDOW_DURATION = Duration
            .ofMinutes(30)
            .toMillis();
    private static final long BUCKET_SIZE = 5_000L;

    private transient ValueState<Integer> scoreState;
    private transient MapState<Long, Integer> windowExpiryState;
    private transient ValueState<ProductDetails> productDetailsState;


    @Override
    public void open(OpenContext openContext) {
        LOG.info("Initializing ProductEventChangeProcessor for subtask...");

        ValueStateDescriptor<Integer> scoreDescriptor =
                new ValueStateDescriptor<>(
                        "score",
                        Types.INT
                );
        MapStateDescriptor<Long, Integer> windowExpiryDescriptor =
                new MapStateDescriptor<>(
                        "windowExpiry",
                        Types.LONG,
                        Types.INT
                );
        ValueStateDescriptor<ProductDetails> productMetadataDescriptor =
                new ValueStateDescriptor<>(
                        "product-details",
                        new AvroTypeInfo<>(ProductDetails.class)
                );

        scoreState = getRuntimeContext().getState(scoreDescriptor);
        windowExpiryState = getRuntimeContext().getMapState(windowExpiryDescriptor);
        productDetailsState = getRuntimeContext().getState(productMetadataDescriptor);
    }

    @Override
    public void processElement1(
            ProductIntersectionEvent value,
            KeyedCoProcessFunction<Long, ProductIntersectionEvent, ProductChangeEvent, ProductScoreEvent>.Context ctx,
            Collector<ProductScoreEvent> out) throws Exception {

        long timestamp = ctx.timestamp();
        long bucketStartTime = timestamp - (timestamp % BUCKET_SIZE);

        int weight = WeightCalculator
                .weightOf(value.getEventType());
        int currentScore = score() + weight;
        scoreState.update(currentScore);
        windowExpiryState.put(bucketStartTime, expiryWeight(bucketStartTime) + weight);

        long evictionTime = bucketStartTime + WINDOW_DURATION;
        ctx.timerService().registerEventTimeTimer(evictionTime);

        long emissionTime = bucketStartTime + BUCKET_SIZE;
        ctx.timerService().registerEventTimeTimer(emissionTime);
    }

    @Override
    public void processElement2(ProductChangeEvent value,
                                KeyedCoProcessFunction<
                                        Long,
                                        ProductIntersectionEvent,
                                        ProductChangeEvent,
                                        ProductScoreEvent>.Context ctx,
                                Collector<ProductScoreEvent> out) throws Exception {
        long productId = ctx.getCurrentKey();
        switch (value.getOperation()) {
            case UPSERT -> {
                LOG.info("Catalog UPSERT received for Product: {}", productId);
                productDetailsState.update(value.getProductDetails());
            }
            case DELETE -> {
                LOG.info("Catalog DELETE received for Product: {}. Clearing all state.", productId);
                productDetailsState.clear();
                scoreState.clear();
                windowExpiryState.clear();
            }
        }
    }

    @Override
    public void onTimer(long timestamp,
                        KeyedCoProcessFunction<
                                Long,
                                ProductIntersectionEvent,
                                ProductChangeEvent,
                                ProductScoreEvent>.OnTimerContext ctx,
                        Collector<ProductScoreEvent> out) throws Exception {

        ProductDetails productDetails = productDetailsState.value();
        long productId = ctx.getCurrentKey();

        if (productDetails != null) {
            long keyToRemove = timestamp - WINDOW_DURATION;
            int currentScore = score();

            if (windowExpiryState.contains(keyToRemove)) {
                int currentWeight = expiryWeight(keyToRemove);
                currentScore -= currentWeight;
                windowExpiryState.remove(keyToRemove);

                if (currentScore <= 0) {
                    scoreState.clear();
                    windowExpiryState.clear();
                    currentScore = 0;
                } else {
                    scoreState.update(currentScore);
                }
            }

            out.collect(ProductScoreEvent.newBuilder()
                    .setEventId(productId + "-" + timestamp)
                    .setProductId(productId)
                    .setScore(currentScore)
                    .setEventTime(Instant.ofEpochMilli(timestamp))
                    .setProductDetails(productDetails)
                    .build());
        } else {
            LOG.warn("Timer fired for Product: {} with score {}, but NO catalog details exist. " +
                    "Interactions arrived before catalog sync. Skipping emission.", productId, score());
        }
    }


    private int score() throws Exception {
        Integer s = scoreState.value();
        return s == null ? 0 : s;
    }

    private int expiryWeight(long key) throws Exception {
        Integer w = windowExpiryState.get(key);
        return w == null ? 0 : w;
    }
}
