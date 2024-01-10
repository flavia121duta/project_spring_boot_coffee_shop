package com.api.project.dto;

import com.api.project.model.ShiftType;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public class ProfileRequest {
    public static final String EMAIL = "^(.+)@(.+)$";
    public static final String PHONE = "^\\d{10}$";

    @NotNull(message = "The email should not be null!")
    @Pattern(regexp = EMAIL, message = "The email is not right.")
    private String email;

    @NotNull(message = "The phone number should not be null!")
    @Pattern(regexp = PHONE, message = "The phone does not have 10 digits.")
    private String phone;
    private String address;

    @Min(1500)
    @Max(value = 10000, message = "Salary should be less than 10000")
    private double salary;
    private ShiftType shiftType;

//    @Past
    private LocalDate dateOfBirth;
    private String linkedinProfile;

    public ProfileRequest() {
    }

    public ProfileRequest(String email, String phone, String address, double salary, ShiftType shiftType, LocalDate dateOfBirth, String linkedinProfile) {
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.salary = salary;
        this.shiftType = shiftType;
        this.dateOfBirth = dateOfBirth;
        this.linkedinProfile = linkedinProfile;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public ShiftType getShiftType() {
        return shiftType;
    }

    public void setShiftType(ShiftType shiftType) {
        this.shiftType = shiftType;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getLinkedinProfile() {
        return linkedinProfile;
    }

    public void setLinkedinProfile(String linkedinProfile) {
        this.linkedinProfile = linkedinProfile;
    }
}
