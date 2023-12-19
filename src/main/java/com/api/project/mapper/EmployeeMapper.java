package com.api.project.mapper;

import com.api.project.dto.EmployeeRequest;
import com.api.project.model.Employee;
import org.springframework.stereotype.Component;

@Component
public class EmployeeMapper {
    public Employee convertRequestToEmployee(EmployeeRequest employeeRequest) {
        return new Employee(
                employeeRequest.getFirstName(),
                employeeRequest.getLastName()
        );
    }
}
