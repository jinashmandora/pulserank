package com.pulserank.common.cache.model;

import java.io.Serializable;
import java.math.BigDecimal;

public record ProductCache(
        Long productId,
        Long categoryId,
        String sku,
        String name,
        String brand,
        BigDecimal price,
        String currency,
        Boolean active
) implements Serializable {
}
