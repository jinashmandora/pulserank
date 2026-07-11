package com.pulserank.eventservice.application.service;

import com.pulserank.eventservice.api.request.PublishEventRequest;
import com.pulserank.eventservice.api.response.PublishEventResponse;
import com.pulserank.eventservice.mapper.ProductEventMapper;
import com.pulserank.eventservice.messaging.producer.ProductEventProducer;
import com.pulserank.schema.event.ProductEvent;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PublishEventService {

    private final ProductEventMapper mapper;
    private final ProductEventProducer producer;

    public PublishEventService(ProductEventMapper mapper, ProductEventProducer producer) {
        this.mapper = mapper;
        this.producer = producer;
    }
    
    public PublishEventResponse publish(PublishEventRequest request) {
        UUID eventId = UUID.randomUUID();
        Instant eventTime = Instant.now();
        ProductEvent event = mapper.toProductEvent(eventId, request, eventTime);
        producer.publish(event);
        return new PublishEventResponse(eventId);
    }

}
