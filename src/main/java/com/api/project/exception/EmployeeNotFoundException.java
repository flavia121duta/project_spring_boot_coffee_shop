package com.api.project.exception;

public class EmployeeNotFoundException extends NotFoundException {
    public EmployeeNotFoundException(int employeeId) {
        super("The employee with the id " + employeeId + " was not found in the database");
    }

    public EmployeeNotFoundException(String lastName) {
        super("No employee with the last name \"" + lastName + "\" was not found in the database");
    }
}
