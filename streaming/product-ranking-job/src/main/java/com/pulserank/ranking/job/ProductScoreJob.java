package com.pulserank.ranking.job;

import com.pulserank.ranking.process.ProductEventChangeProcessor;
import com.pulserank.ranking.sink.KafkaSinkFactory;
import com.pulserank.ranking.source.KafkaSourceFactory;
import com.pulserank.schema.event.ProductChangeEvent;
import com.pulserank.schema.event.ProductIntersectionEvent;
import org.apache.flink.api.common.eventtime.WatermarkStrategy;
import org.apache.flink.streaming.api.datastream.DataStream;
import org.apache.flink.streaming.api.environment.StreamExecutionEnvironment;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Duration;

public class ProductScoreJob {

    private static final Logger LOG = LoggerFactory.getLogger(ProductScoreJob.class);

    public void execute(StreamExecutionEnvironment environment) {

        WatermarkStrategy<ProductIntersectionEvent> productIntersectionEventWatermarkStrategy = WatermarkStrategy
                .<ProductIntersectionEvent>forBoundedOutOfOrderness(Duration.ofMinutes(1))
                .withTimestampAssigner((event, timestamp) ->
                        event.getEventTime().toEpochMilli())
                .withIdleness(Duration.ofMinutes(1));

        WatermarkStrategy<ProductChangeEvent> productChangeEventWatermarkStrategy = WatermarkStrategy
                .<ProductChangeEvent>forBoundedOutOfOrderness(Duration.ofSeconds(5))
                .withTimestampAssigner((element, recordTimestamp) ->
                        element.getEventTime().toEpochMilli())
                .withIdleness(Duration.ofMinutes(1));


        DataStream<ProductIntersectionEvent> productInteractions = environment.fromSource(
                KafkaSourceFactory.productIntersections(),
                productIntersectionEventWatermarkStrategy,
                "source-product-interactions"
        ).uid("source-product-interactions");

        DataStream<ProductChangeEvent> productChangeEvents = environment.fromSource(
                KafkaSourceFactory.productChanges(),
                productChangeEventWatermarkStrategy,
                "source-product-changes"
        ).uid("source-product-changes");


        productInteractions
                .keyBy(ProductIntersectionEvent::getProductId)
                .connect(productChangeEvents.keyBy(ProductChangeEvent::getProductId))
                .process(new ProductEventChangeProcessor())
                .uid("proccess-rolling-window-score")
                .sinkTo(KafkaSinkFactory.productScoreSink())
                .uid("sink-product-score")
                .name("kafka-product-score");
    }
}
