package com.pulserank.catalog.model;

import java.math.BigDecimal;

public record CreateProductRequest(
        String sku,
        String name,
        String description,
        Long categoryId,
        String brand,
        BigDecimal price,
        String currency
) {
}
