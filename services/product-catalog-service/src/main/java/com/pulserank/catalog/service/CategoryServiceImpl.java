package com.pulserank.catalog.service;

import com.pulserank.catalog.enity.Category;
import com.pulserank.catalog.mapper.CategoryMapper;
import com.pulserank.catalog.model.CategoryResponse;
import com.pulserank.catalog.model.CreateCategoryRequest;
import com.pulserank.catalog.repository.CategoryRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final CategoryMapper categoryMapper;

    public CategoryServiceImpl(
            CategoryRepository categoryRepository,
            CategoryMapper categoryMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.categoryMapper = categoryMapper;
    }

    @Override
    public List<CategoryResponse> getCategories() {
        return categoryRepository.findAll()
                .stream()
                .map(categoryMapper::toCategoryResponse)
                .toList();
    }

    @Override
    public CategoryResponse save(CreateCategoryRequest request) {
        Category category = categoryRepository.save(new Category(
                request.name(),
                request.description()
        ));
        return new CategoryResponse(
                category.getId(),
                category.getName()
        );
    }
}
