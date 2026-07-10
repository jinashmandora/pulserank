package com.pulserank.eventservice.controller;

import com.pulserank.eventservice.model.ProductInteractionEventRequest;
import com.pulserank.eventservice.model.ProductInteractionEventResponse;
import com.pulserank.eventservice.service.ProductInteractionEventServiceImpl;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
public class ProductEventController {

    private final ProductInteractionEventServiceImpl productInteractionEventServiceImpl;

    public ProductEventController(ProductInteractionEventServiceImpl productInteractionEventServiceImpl) {
        this.productInteractionEventServiceImpl = productInteractionEventServiceImpl;
    }

    @PostMapping(value = "/product-interactions")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public ProductInteractionEventResponse publish(@Valid @RequestBody ProductInteractionEventRequest productInteractionEventRequest) {
        return productInteractionEventServiceImpl.publish(productInteractionEventRequest);
    }

}
