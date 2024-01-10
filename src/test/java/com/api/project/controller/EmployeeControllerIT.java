package com.api.project.controller;

import com.api.project.dto.EmployeeRequest;
import com.api.project.mapper.EmployeeMapper;
import com.api.project.model.Employee;
import com.api.project.model.Profile;
import com.api.project.model.ShiftType;
import com.api.project.service.EmployeeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = EmployeeRestController.class)
public class EmployeeControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private EmployeeService employeeService;

    @MockBean
    private EmployeeMapper employeeMapper;

    @Test
    @DisplayName("Create employee without profile from Controller - integration test")
    public void createEmployeeWithoutProfile() throws Exception {
        // arrange
        EmployeeRequest employeeRequest = new EmployeeRequest("John", "Doe");

        // arrange
        Employee theEmployee = new Employee("John", "Doe");
        when(employeeService.createEmployee(any(Employee.class))).thenReturn(theEmployee);
        when(employeeMapper.convertRequestToEmployee(any(EmployeeRequest.class))).thenReturn(theEmployee);

        // act & assert
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$.firstName").value(theEmployee.getFirstName()));
    }

    @Test
    @DisplayName("Create employee with profile from Controller - integration test")
    public void createEmployeeWithProfile() throws Exception {
        // arrange
        EmployeeRequest employeeRequest = new EmployeeRequest("John", "Doe");

        int profileId = 123;

        Employee createdEmployee = new Employee("John", "Doe");
        createdEmployee.setEmployeeId(1);

        // mock the service behavior
        when(employeeMapper.convertRequestToEmployee(any(EmployeeRequest.class))).thenReturn(createdEmployee);
        when(employeeService.createEmployeeWithProfile(createdEmployee, profileId)).thenReturn(createdEmployee);


        // act & assert
        mockMvc.perform(post("/employees")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest))
                        .param("profileId", String.valueOf(profileId)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.firstName").value("John"))
                .andExpect(jsonPath("$.lastName").value("Doe"));
    }

    @Test
    @DisplayName("Find employee by id - integration test")
    public void findEmployeeById() throws Exception {
        // arrange
        int employeeId = 1;
        Employee foundEmployee = new Employee();
        foundEmployee.setEmployeeId(employeeId);
        foundEmployee.setFirstName("John");

        when(employeeService.findById(employeeId)).thenReturn(Optional.of(foundEmployee));

        // act & assert
        mockMvc.perform(get("/employees/{employeeId}", employeeId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.employeeId").value(employeeId))
                .andExpect(jsonPath("$.firstName").value("John"));
    }

    @Test
    @DisplayName("The employee is not found - integration test")
    public void getEmployeeById_employeeNotFound() throws Exception {
        // arrange
        int invalidEmployeeId = 99;

        when(employeeService.findById(invalidEmployeeId)).thenReturn(Optional.empty());

        // act & assert
        mockMvc.perform(get("/employees/{invalidEmployeeId}", invalidEmployeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
//                .andExpect(status().isNotFound())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

        verify(employeeService).findById(invalidEmployeeId);
    }

    @Test
    public void findAllEmployees() throws Exception {
        // arrange
        Employee employee1 = new Employee("John", "Doe");
        Employee employee2 = new Employee("Jane", "Doe");
        Employee employee3 = new Employee("Sabrina", "Smith");

        List<Employee> existingEmployees = List.of(employee1, employee2, employee3);
        when(employeeService.findAll()).thenReturn(existingEmployees);

        // act & assert
        mockMvc.perform(get("/employees"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[2].firstName").value("Sabrina"));
    }

    @Test
    public void findAllEmployeesByLastName() throws Exception {
        // arrange
        Employee employee1 = new Employee("John", "Doe");
        Employee employee2 = new Employee("Jane", "Doe");

        List<Employee> employeesWithLastNameDoe = List.of(employee1, employee2);
        when(employeeService.findByLastName("Doe")).thenReturn(employeesWithLastNameDoe);

        // act & assert
        mockMvc.perform(get("/employees?lastName=Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    public void findAllEmployeesByLastName_noEmployeeFound() throws Exception {
        // arrange

        // act
        when(employeeService.findByLastName("Doe")).thenReturn(Collections.emptyList());

        // assert
        mockMvc.perform(get("/employees")
                        .param("lastName", "Doe"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$").isEmpty());
    }

    @Test
    @DisplayName("Find all employees above the salary - integration test")
    public void findEmployeesAboveSalary() throws Exception {
        // arrange
        double salaryThreshold = 2300.0;

        Employee employee1 = new Employee("John", "Doe");
        Profile profile1 = new Profile();
        profile1.setSalary(2350);
        employee1.setProfile(profile1);

        Employee employee2 = new Employee("Jane", "Smith");
        Profile profile2 = new Profile();
        profile2.setSalary(2400);
        employee2.setProfile(profile2);

        List<Employee> listOfEmployeesThatHaveSalaryAboveThreshold = List.of(employee1, employee2);

        when(employeeService.findEmployeesAboveSalary(salaryThreshold)).thenReturn(listOfEmployeesThatHaveSalaryAboveThreshold);

        // act & assert
        mockMvc.perform(get("/employees/above-salary/{theSalary}", salaryThreshold))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Smith"));
    }

    @Test
    @DisplayName("Find all employees that works in a certain shift type - integration test")
    public void findEmployeesByShiftType() throws Exception {
        // arrange
        ShiftType shiftType = ShiftType.FULL_TIME;

        Employee employee1 = new Employee("John", "Doe");
        Profile profile1 = new Profile();
        profile1.setShiftType(ShiftType.FULL_TIME);
        employee1.setProfile(profile1);

        Employee employee2 = new Employee("Jane", "Doe");
        Profile profile2 = new Profile();
        profile2.setShiftType(ShiftType.FULL_TIME);
        employee2.setProfile(profile2);

        when(employeeService.findEmployeesByShiftType(shiftType)).thenReturn(List.of(employee1, employee2));

        // act & assert
        mockMvc.perform(get("/employees/shifts/{shiftType}", shiftType))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"));
    }

    @Test
    public void findEmployeesWithNoSalesTaken() throws Exception {
        // arrange
        Employee employee1 = new Employee("John", "Doe");
        Employee employee2 = new Employee("Jane", "Down");

        // act
        when(employeeService.findEmployeesWithNoSalesTaken()).thenReturn(List.of(employee1, employee2));

        // assert
        mockMvc.perform(get("/employees/no-sales"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].firstName").value("John"))
                .andExpect(jsonPath("$[0].lastName").value("Doe"))
                .andExpect(jsonPath("$[1].firstName").value("Jane"))
                .andExpect(jsonPath("$[1].lastName").value("Down"));
    }


    @Test
    public void updateEmployeeWithProfile() throws Exception {
        // arrange
        EmployeeRequest employeeRequest = new EmployeeRequest("John", "Doe");
        int employeeId = 1;
        int profileId = 100;

        Employee updatedEmployee = new Employee("John", "Down");
        updatedEmployee.setEmployeeId(employeeId);

        when(employeeMapper.convertRequestToEmployee(employeeRequest)).thenReturn(updatedEmployee);
        when(employeeService.updateEmployeeWithProfile(updatedEmployee, employeeId, profileId)).thenReturn(updatedEmployee);

        // act & assert
        mockMvc.perform(put("/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest))
                        .param("profileId", String.valueOf(profileId)))
                .andExpect(status().is2xxSuccessful());
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.employeeId").value(updatedEmployee.getEmployeeId()))
//                .andExpect(jsonPath("$.firstName").value(updatedEmployee.getFirstName()))
//                .andExpect(jsonPath("$.lastName").value(updatedEmployee.getLastName()));
    }

    @Test
    public void updateEmployeeWithoutProfile() throws Exception {
        // arrange
        EmployeeRequest employeeRequest = new EmployeeRequest("John", "Doe");
        int employeeId = 1;

        Employee updatedEmployee = new Employee("John", "Down");
        updatedEmployee.setEmployeeId(employeeId);

        // Mock the service behavior
        when(employeeMapper.convertRequestToEmployee(employeeRequest)).thenReturn(updatedEmployee);
        when(employeeService.updateEmployee(updatedEmployee, employeeId)).thenReturn(updatedEmployee);

        // act & assert
        String responseContent = mockMvc.perform(put("/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(employeeRequest)))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();
//                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
//                .andExpect(jsonPath("$.firstName").value(updatedEmployee.getFirstName()))
//                .andExpect(jsonPath("$.lastName").value(updatedEmployee.getLastName()));

//        System.out.println("Response Content: " + responseContent);
    }

    @Test
    public void deleteEmployeeByEmployeeId() throws Exception {
        // arrange
        int employeeId = 1;

        doNothing().when(employeeService).deleteById(employeeId);

        // act & arrange
        mockMvc.perform(delete("/employees/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().string("The employee with the id: " + employeeId + " was successfully deleted."));
        verify(employeeService).deleteById(employeeId);
    }

    @Test
    public void deleteEmployeesThatWorkedSeasonalShit() throws Exception {
        // arrange

        // act & assert
        mockMvc.perform(delete("/employees/seasonal")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService).deleteByShiftTypeSeasonal();
    }

    @Test
    public void deleteAllEmployees() throws Exception {
        // arrange
        doNothing().when(employeeService).deleteAll();

        // act & assert
        mockMvc.perform(delete("/employees")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(employeeService).deleteAll();
    }
}
