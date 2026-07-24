package com.pulserank.catalog.model;

import java.math.BigDecimal;

public record ProductResponse(
        Long id,
        String name,
        String sku,
        String description,
        Long categoryId,
        String brand,
        BigDecimal price,
        String currency
) {
}
