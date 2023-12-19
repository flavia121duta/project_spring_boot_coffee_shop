package com.api.project.exception;

public class EmployeeNotFoundException extends NotFoundException {
    public EmployeeNotFoundException(String message) {
        super(message);
    }
}
