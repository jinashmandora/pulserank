package com.pulserank.catalog.mapper;

import com.pulserank.catalog.enity.Category;
import com.pulserank.catalog.model.CategoryResponse;
import org.springframework.stereotype.Component;

@Component
public class CategoryMapper {

    public CategoryResponse toCategoryResponse(Category category) {
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
