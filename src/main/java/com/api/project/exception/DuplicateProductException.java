package com.api.project.exception;

public class DuplicateProductException extends RuntimeException {
    public DuplicateProductException(String productName) {
        super("A product with the same name '" + productName + "' already exists.");
    }
}
