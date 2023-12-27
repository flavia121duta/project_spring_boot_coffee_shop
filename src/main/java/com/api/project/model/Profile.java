package com.api.project.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.Period;

@Entity
public class Profile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int profileId;
    private String email;
    private String phone;
    private String address;
    private double salary;
    @Enumerated(EnumType.STRING)
    @Column(name = "shift_type")
    private ShiftType shiftType;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "linkedin_profile")
    private String linkedinProfile;

    @OneToOne(mappedBy = "profile", cascade = {
            CascadeType.MERGE,
            CascadeType.PERSIST
    })
    private Employee employee;

    public Profile() {}

    public Profile(String email, String phone, String address, double salary, ShiftType shiftType, LocalDate dateOfBirth, String linkedinProfile) {
        this.email = email;
        this.phone = phone;
        this.address = address;
        this.salary = salary;
        this.shiftType = shiftType;
        this.dateOfBirth = dateOfBirth;
        this.linkedinProfile = linkedinProfile;
    }

    public int getProfileId() {
        return profileId;
    }

    public void setProfileId(int profileId) {
        this.profileId = profileId;
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

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public int getAge() {
        LocalDate today = LocalDate.now();

        if (dateOfBirth != null) {
            return Period.between(dateOfBirth, today).getYears();
        } else {
            return 0;
        }
    }

    @Override
    public String toString() {
        return "Profile{" +
                "profileId=" + profileId +
                ", email='" + email + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", salary=" + salary +
                ", shiftType=" + shiftType +
                ", dateOfBirth=" + dateOfBirth +
                ", linkedinProfile='" + linkedinProfile + '\'' +
                '}';
    }
}
