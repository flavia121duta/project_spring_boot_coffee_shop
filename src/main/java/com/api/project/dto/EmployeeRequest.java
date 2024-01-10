package com.api.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class EmployeeRequest {
    @NotNull(message = "First name should not be null!")
    @NotBlank(message = "First name should not be blank!")
    private String firstName;

    @NotNull(message = "Last name should not be null!")
    @NotBlank(message = "Last name should not be blank!")
    private String lastName;

    public EmployeeRequest() {
    }

    public EmployeeRequest(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
