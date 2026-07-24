package com.pulserank.eventservice.mapper;

import com.pulserank.common.cache.model.ProductCache;
import com.pulserank.eventservice.model.PublishEventRequest;
import com.pulserank.schema.event.ProductEvent;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.UUID;

@Component
public class ProductEventMapper {

    public ProductEvent toProductEvent(
            UUID eventId,
            PublishEventRequest request,
            ProductCache productCache,
            Instant eventTime
    ) {
        return ProductEvent.newBuilder()
                .setEventId(eventId.toString())
                .setUserId(request.userId().toString())
                .setProductId(request.productId())
                .setCategoryId(productCache.categoryId())
                .setName(productCache.name())
                .setBrand(productCache.brand())
                .setPrice(productCache.price())
                .setCurrency(productCache.currency())
                .setEventType(request.eventType())
                .setEventTime(eventTime)
                .build();
    }

}
