package com.api.project.exception;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException(String message) {
        super(message);
    }
}
