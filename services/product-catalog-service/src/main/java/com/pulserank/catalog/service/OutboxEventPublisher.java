package com.pulserank.catalog.service;

import com.pulserank.catalog.config.AvroKafkaSerializerConfig;
import com.pulserank.catalog.enity.ProductsOutbox;
import com.pulserank.catalog.model.ProductOutboxEvent;
import com.pulserank.catalog.repository.OutboxRepository;
import com.pulserank.common.kafka.KafkaTopics;
import io.apicurio.registry.serde.avro.AvroKafkaSerializer;
import org.apache.avro.specific.SpecificRecord;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class OutboxEventPublisher {

    private final AvroKafkaSerializerConfig serializerConfig;
    private final OutboxRepository outboxRepository;

    public OutboxEventPublisher(
            AvroKafkaSerializerConfig serializerConfig,
            OutboxRepository outboxRepository) {
        this.serializerConfig = serializerConfig;
        this.outboxRepository = outboxRepository;
    }

    public void publish(ProductOutboxEvent message) {
        var eventId = UUID.randomUUID();
        var eventTime = Instant.now();

        var avroPayload = message.avroSchema()
                .apply(eventId.toString(), eventTime);

        try (AvroKafkaSerializer<SpecificRecord> avroKafkaSerializer = serializerConfig
                .catalogEventSerializer()) {
            byte[] payloadBytes = avroKafkaSerializer
                    .serialize(KafkaTopics.CATALOG_PRODUCT_CHANGES_V1, avroPayload);

            var outbox = new ProductsOutbox(
                    eventId,
                    message.productId(),
                    message.operationType(),
                    payloadBytes,
                    eventTime
            );

            outboxRepository.save(outbox);
        }
    }
}
