package com.api.project.service;

import com.api.project.model.Employee;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK)
public class EmployeeServiceIT {
    @Autowired
    private EmployeeService employeeService;

    @Test
    @DisplayName("Create employee it...")
    public void createEmployeeTest() {
        // arrange
        Employee newEmployee = new Employee("Mihai", "Dascalescu");

        // act
        Employee result = employeeService.createEmployee(newEmployee);

        // assert
        assertEquals(newEmployee, result, "Should be equals");
    }
}
