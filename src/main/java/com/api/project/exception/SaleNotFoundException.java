package com.api.project.exception;

public class SaleNotFoundException extends NotFoundException {
    public SaleNotFoundException(int saleId) {
        super("The sale with the id " + saleId + " was not found in the database");
    }
}
