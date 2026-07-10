package com.pulserank.eventservice.mapper;

import com.pulserank.eventservice.model.ProductInteractionEventRequest;
import com.pulserank.schema.event.ProductIntersectionEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ProductInteractionEventMapper {

    public ProductIntersectionEvent map(
            UUID eventId,
            ProductInteractionEventRequest request,
            Instant eventTime
    ) {
        return ProductIntersectionEvent.newBuilder()
                .setEventId(eventId.toString())
                .setUserId(request.userId().toString())
                .setProductId(request.productId())
                .setEventType(request.eventType())
                .setEventTime(eventTime)
                .build();
    }

}
