package com.api.project.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public class CustomerRequest {
    public static final String EMAIL = "^(.+)@(.+)$";

    @NotBlank(message = "The name of the customer cannot be null")
    private String fullName;

    @Pattern(regexp = EMAIL)
    private String email;

    public CustomerRequest() {
    }

    public CustomerRequest(String fullName, String email) {
        this.fullName = fullName;
        this.email = email;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
