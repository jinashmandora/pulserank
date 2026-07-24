package com.pulserank.ranking.source;

import com.pulserank.ranking.serialization.ApiCurioKafkaRecordDeserializationSchema;
import com.pulserank.schema.event.ProductChangeEvent;
import com.pulserank.schema.event.ProductIntersectionEvent;
import org.apache.flink.connector.kafka.source.KafkaSource;
import org.apache.flink.connector.kafka.source.enumerator.initializer.OffsetsInitializer;

import static com.pulserank.common.kafka.KafkaConfiguration.KAFKA_BOOTSTRAP_SERVERS;
import static com.pulserank.common.kafka.KafkaTopics.CATALOG_PRODUCT_CHANGES_V1;
import static com.pulserank.common.kafka.KafkaTopics.CATALOG_PRODUCT_INTERACTIONS_V1;
import static com.pulserank.ranking.config.JobConfiguration.CG_PRODUCT_CHANGES;
import static com.pulserank.ranking.config.JobConfiguration.CG_PRODUCT_INTERACTIONS;

public final class KafkaSourceFactory {

    private KafkaSourceFactory() {

    }

    public static KafkaSource<ProductIntersectionEvent> productIntersections() {
        return of(
                CATALOG_PRODUCT_INTERACTIONS_V1,
                CG_PRODUCT_INTERACTIONS,
                ProductIntersectionEvent.class
        );
    }

    public static KafkaSource<ProductChangeEvent> productChanges() {
        return of(
                CATALOG_PRODUCT_CHANGES_V1,
                CG_PRODUCT_CHANGES,
                ProductChangeEvent.class
        );
    }

    private static <T> KafkaSource<T> of(
            String topic,
            String groupId,
            Class<T> targetType
    ) {
        return KafkaSource.<T>builder()
                .setBootstrapServers(KAFKA_BOOTSTRAP_SERVERS)
                .setTopics(topic)
                .setGroupId(groupId)
                .setStartingOffsets(OffsetsInitializer.earliest())
                .setDeserializer(new ApiCurioKafkaRecordDeserializationSchema<>(
                        targetType
                ))
                .build();
    }

}
