package com.api.project.exception;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException(int customerId) {
        super("The customer with the id " + customerId + " was not found in the database");
    }
}
