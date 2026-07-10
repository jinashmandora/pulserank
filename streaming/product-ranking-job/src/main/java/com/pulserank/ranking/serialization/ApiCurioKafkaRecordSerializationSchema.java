package com.pulserank.ranking.serialization;

import com.pulserank.ranking.config.JobConfiguration;
import io.apicurio.registry.serde.avro.AvroKafkaSerializer;
import org.apache.flink.api.common.serialization.SerializationSchema;
import org.apache.flink.api.java.functions.KeySelector;
import org.apache.flink.connector.kafka.sink.KafkaRecordSerializationSchema;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.jspecify.annotations.Nullable;

import java.nio.charset.StandardCharsets;

public class ApiCurioKafkaRecordSerializationSchema<T> implements KafkaRecordSerializationSchema<T> {

    private final String topic;
    private final KeySelector<T, String> keySelector;

    private transient AvroKafkaSerializer<T> avroKafkaSerializer;

    public ApiCurioKafkaRecordSerializationSchema(
            String topic,
            KeySelector<T, String> keySelector
    ) {
        this.topic = topic;
        this.keySelector = keySelector;
    }

    @Override
    public void open(SerializationSchema.InitializationContext context, KafkaSinkContext sinkContext) throws Exception {
        avroKafkaSerializer = new AvroKafkaSerializer<>();
        avroKafkaSerializer.configure(JobConfiguration.apicurioConfig(), false);
    }

    @Override
    public @Nullable ProducerRecord<byte[], byte[]> serialize(T element, KafkaSinkContext context, Long timestamp) {
        byte[] valueBytes = avroKafkaSerializer.serialize(topic, element);
        try {
            String key = keySelector.getKey(element);
            byte[] keyBytes = key != null ? key.getBytes(StandardCharsets.UTF_8) : null;
            return new ProducerRecord<>(
                    topic,
                    null,
                    timestamp,
                    keyBytes,
                    valueBytes
            );
        } catch (Exception e) {
            throw new RuntimeException("Failed to extract Kafka key from element", e);
        }
    }

}
