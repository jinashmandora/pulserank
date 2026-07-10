package com.pulserank.catalog.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException() {
        super("Product not found exception");
    }
}
