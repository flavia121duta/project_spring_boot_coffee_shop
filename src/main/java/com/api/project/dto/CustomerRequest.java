package com.api.project.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

@ApiModel(value = "Customer Request", description = "Required details needed to create a new Customer")
public class CustomerRequest {
    public static final String EMAIL = "^(.+)@(.+)$";

    @NotBlank(message = "The name of the customer cannot be null")
    @ApiModelProperty(value = "full name", required = true, notes = "The first and last name of the Customer, separated by space", example = "John Doe", position = 1)
    private String fullName;

    @Pattern(regexp = EMAIL)
    @ApiModelProperty(value = "email", required = true, notes = "The email of the Customer", example = "john_doe@email.com", position = 2)
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
