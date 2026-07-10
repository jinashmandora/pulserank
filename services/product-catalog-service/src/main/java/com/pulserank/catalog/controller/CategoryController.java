package com.pulserank.catalog.controller;

import com.pulserank.catalog.model.CategoryResponse;
import com.pulserank.catalog.model.CreateCategoryRequest;
import com.pulserank.catalog.service.CategoryService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/categories")
public class CategoryController {

    private final CategoryService categoryService;

    public CategoryController(CategoryService categoryService) {
        this.categoryService = categoryService;
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<CategoryResponse> getCategories() {
        return categoryService.getCategories();
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CategoryResponse create(@RequestBody CreateCategoryRequest request) {
        return categoryService.save(request);
    }
}
