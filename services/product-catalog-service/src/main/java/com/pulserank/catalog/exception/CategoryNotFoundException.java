package com.pulserank.catalog.exception;

public class CategoryNotFoundException extends RuntimeException {

    public CategoryNotFoundException() {
        super("Category not found exception");
    }
}
