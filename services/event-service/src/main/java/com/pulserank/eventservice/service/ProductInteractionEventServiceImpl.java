package com.pulserank.eventservice.service;

import com.pulserank.eventservice.mapper.ProductInteractionEventMapper;
import com.pulserank.eventservice.model.ProductInteractionEventRequest;
import com.pulserank.eventservice.model.ProductInteractionEventResponse;
import com.pulserank.schema.event.ProductIntersectionEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

import static com.pulserank.common.kafka.KafkaTopics.CATALOG_PRODUCT_INTERACTIONS_V1;

@Service
public class ProductInteractionEventServiceImpl implements ProductInteractionEventService {

    private static final Logger LOG = LoggerFactory.getLogger(ProductInteractionEventServiceImpl.class);

    private final KafkaTemplate<String, ProductIntersectionEvent> kafkaTemplate;
    private final ProductInteractionEventMapper productInteractionEventMapper;

    public ProductInteractionEventServiceImpl(
            KafkaTemplate<String, ProductIntersectionEvent> kafkaTemplate,
            ProductInteractionEventMapper productInteractionEventMapper
    ) {
        this.kafkaTemplate = kafkaTemplate;
        this.productInteractionEventMapper = productInteractionEventMapper;
    }

    public ProductInteractionEventResponse publish(
            ProductInteractionEventRequest request) {

        UUID eventId = UUID.randomUUID();
        Instant eventTime = Instant.now();

        ProductIntersectionEvent event = productInteractionEventMapper.map(
                eventId,
                request,
                eventTime
        );

        LOG.info("Publishing ProductEvent eventId={} productId={} eventType={}",
                event.getEventId(),
                event.getProductId(),
                event.getEventType()
        );
        kafkaTemplate.send(CATALOG_PRODUCT_INTERACTIONS_V1, event)
                .thenAccept(result -> LOG.info(
                        "Published ProductInteractionEvent eventId={} partition={} offset={}",
                        event.getEventId(),
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset())
                )
                .exceptionally(ex -> {
                    LOG.error(
                            "Failed to publish ProductInteractionEvent eventId={}",
                            event.getEventId(),
                            ex
                    );
                    return null;
                });
        return new ProductInteractionEventResponse(eventId);
    }

}
