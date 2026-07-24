package com.pulserank.catalog.exception;

public class DuplicateProductException extends RuntimeException {

    public DuplicateProductException(String sku) {
        super("Duplicate product exception");
    }
}
