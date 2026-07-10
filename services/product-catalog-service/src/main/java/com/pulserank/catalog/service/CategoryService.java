package com.pulserank.catalog.service;


import com.pulserank.catalog.model.CategoryResponse;
import com.pulserank.catalog.model.CreateCategoryRequest;

import java.util.List;

public interface CategoryService {

    List<CategoryResponse> getCategories();

    CategoryResponse save(CreateCategoryRequest request);
}
