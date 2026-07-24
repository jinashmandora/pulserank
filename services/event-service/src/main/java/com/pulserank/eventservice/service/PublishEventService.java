package com.pulserank.eventservice.service;

import com.pulserank.eventservice.mapper.ProductEventMapper;
import com.pulserank.eventservice.model.PublishEventRequest;
import com.pulserank.eventservice.model.PublishEventResponse;
import com.pulserank.eventservice.producer.ProductEventProducer;
import com.pulserank.eventservice.repository.ProductCacheRepository;
import com.pulserank.schema.event.ProductEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class PublishEventService {

    private static final Logger LOG = LoggerFactory.getLogger(PublishEventService.class);
    private final ProductEventMapper mapper;
    private final ProductEventProducer producer;
    private final ProductCacheRepository productCacheRepository;

    public PublishEventService(
            ProductEventMapper mapper,
            ProductEventProducer producer,
            ProductCacheRepository productCacheRepository
    ) {
        this.mapper = mapper;
        this.producer = producer;
        this.productCacheRepository = productCacheRepository;
    }

    public PublishEventResponse publish(PublishEventRequest request) {
        UUID eventId = UUID.randomUUID();
        productCacheRepository.get(request.productId())
                .ifPresentOrElse(cache -> {
                            Instant eventTime = Instant.now();
                            ProductEvent event = mapper.toProductEvent(
                                    eventId,
                                    request,
                                    cache,
                                    eventTime
                            );
                            producer.publish(event);
                        },
                        () -> {
                            LOG.warn("product:{} not found in the cache", request.productId());
                        });
        return new PublishEventResponse(eventId);
    }

}
