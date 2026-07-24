package com.pulserank.eventservice.controller;

import com.pulserank.eventservice.model.PublishEventRequest;
import com.pulserank.eventservice.model.PublishEventResponse;
import com.pulserank.eventservice.service.PublishEventService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/events")
public class PublishEventController {

    private final PublishEventService publishEventService;

    public PublishEventController(PublishEventService publishEventService) {
        this.publishEventService = publishEventService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public PublishEventResponse publish(@Valid @RequestBody PublishEventRequest publishEventRequest) {
        return publishEventService.publish(publishEventRequest);
    }

}
