package com.pulserank.ranking.utils;

import com.pulserank.schema.event.EventType;

public final class WeightCalculator {

    private WeightCalculator() {
    }

    public static int weightOf(EventType eventType) {
        return switch (eventType) {
            case VIEW -> 1;
            case CLICK -> 3;
            case WISHLIST -> 5;
            case CART -> 10;
            case PURCHASE -> 30;
        };
    }
}
