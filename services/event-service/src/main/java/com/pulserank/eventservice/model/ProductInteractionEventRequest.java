package com.pulserank.eventservice.model;

import com.pulserank.schema.event.EventType;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record ProductInteractionEventRequest(
        @NotNull
        UUID userId,

        @NotNull
        Long productId,

        @NotNull
        EventType eventType
) {
}
