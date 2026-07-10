package com.pulserank.catalog.model;

public record CreateCategoryRequest(
        String name,
        String description
) {
}
