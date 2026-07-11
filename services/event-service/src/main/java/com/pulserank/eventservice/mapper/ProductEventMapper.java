package com.pulserank.eventservice.mapper;

import com.pulserank.eventservice.api.request.PublishEventRequest;
import com.pulserank.schema.event.ProductEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ProductEventMapper {

    public ProductEvent toProductEvent(
            UUID eventId,
            PublishEventRequest request,
            Instant eventTime
    ) {
        return ProductEvent.newBuilder()
                .setEventId(eventId.toString())
                .setUserId(request.userId().toString())
                .setProductId(request.productId().toString())
                .setEventType(request.eventType())
                .setEventTime(eventTime)
                .build();
    }

}
