package com.pulserank.eventservice.api.request;

import com.pulserank.schema.event.EventType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record PublishEventRequest(
        @NotNull
        UUID userId,

        @NotNull
        UUID productId,

        @NotNull
        EventType eventType
) {
}
