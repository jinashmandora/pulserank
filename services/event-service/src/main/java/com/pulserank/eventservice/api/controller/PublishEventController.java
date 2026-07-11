package com.pulserank.eventservice.api.controller;

import com.pulserank.eventservice.api.request.PublishEventRequest;
import com.pulserank.eventservice.api.response.PublishEventResponse;
import com.pulserank.eventservice.application.service.PublishEventService;
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
