package com.pulserank.ranking.sink;

import com.pulserank.common.kafka.KafkaTopics;
import com.pulserank.ranking.serialization.ApiCurioKafkaRecordSerializationSchema;
import com.pulserank.schema.event.ProductScoreEvent;
import org.apache.flink.connector.base.DeliveryGuarantee;
import org.apache.flink.connector.kafka.sink.KafkaSink;

import static com.pulserank.common.kafka.KafkaConfiguration.KAFKA_BOOTSTRAP_SERVERS;

public final class KafkaSinkFactory {

    private KafkaSinkFactory() {

    }

    public static KafkaSink<ProductScoreEvent> productScoreSink() {
        return KafkaSink.<ProductScoreEvent>builder()
                .setBootstrapServers(KAFKA_BOOTSTRAP_SERVERS)
                .setRecordSerializer(new ApiCurioKafkaRecordSerializationSchema<>(
                        KafkaTopics.ANALYTIC_PRODUCT_SCORES_V1,
                        (productScoreEvent) -> String.valueOf(productScoreEvent.getProductId())
                ))
                .setDeliveryGuarantee(DeliveryGuarantee.AT_LEAST_ONCE)
                .build();
    }

}
