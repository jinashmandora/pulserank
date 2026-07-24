package com.pulserank.eventservice.producer;

import com.pulserank.schema.event.ProductEvent;

public interface ProductEventProducer {

    void publish(ProductEvent event);

}
