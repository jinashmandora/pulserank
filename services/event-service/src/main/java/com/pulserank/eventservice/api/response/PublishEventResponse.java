package com.pulserank.eventservice.api.response;

import java.util.UUID;

public record PublishEventResponse(
        UUID eventId
) {
}
