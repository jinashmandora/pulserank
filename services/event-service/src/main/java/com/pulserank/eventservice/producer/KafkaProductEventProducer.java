package com.pulserank.eventservice.producer;

import com.pulserank.schema.event.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
public class KafkaProductEventProducer implements ProductEventProducer {
    private static final Logger log = LoggerFactory.getLogger(KafkaProductEventProducer.class);
    private static final String TOPIC_NAME = "product-events";
    private final KafkaTemplate<String, ProductEvent> kafkaTemplate;

    public KafkaProductEventProducer(
            KafkaTemplate<String, ProductEvent> kafkaTemplate
    ) {
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publish(ProductEvent event) {
        log.info(
                "Publishing ProductEvent eventId={} productId={} eventType={}",
                event.getEventId(),
                event.getProductId(),
                event.getEventType()
        );
        kafkaTemplate.send(TOPIC_NAME, event)
                .thenAccept(result -> log.info(
                        "Published ProductEvent eventId={} partition={} offset={}",
                        event.getEventId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset())
                )
                .exceptionally(ex -> {
                    log.error(
                            "Failed to publish ProductEvent eventId={}",
                            event.getEventId(),
                            ex
                    );
                    return null;
                });
    }
}
