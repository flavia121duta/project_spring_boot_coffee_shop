package com.api.project.exception;

public class ProductNotFoundException extends NotFoundException {
    public ProductNotFoundException(int productId) {
        super("The product with the id " + productId + " was not found in the database");
    }

    public ProductNotFoundException(String productName) {
        super("The product with the name " + productName + " was not found in the database");
    }
}
