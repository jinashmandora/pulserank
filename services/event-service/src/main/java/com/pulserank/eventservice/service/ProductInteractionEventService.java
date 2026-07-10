package com.pulserank.eventservice.service;

import com.pulserank.eventservice.model.ProductInteractionEventRequest;
import com.pulserank.eventservice.model.ProductInteractionEventResponse;

public interface ProductInteractionEventService {

    ProductInteractionEventResponse publish(ProductInteractionEventRequest request);
}
