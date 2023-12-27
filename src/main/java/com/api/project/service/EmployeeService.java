package com.api.project.service;

import com.api.project.exception.EmployeeNotFoundException;
import com.api.project.exception.ProfileNotFoundException;
import com.api.project.model.Employee;
import com.api.project.model.Profile;
import com.api.project.model.ShiftType;
import com.api.project.repository.EmployeeRepository;
import com.api.project.repository.ProfileRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployeeService {

    private final EmployeeRepository employeeRepository;
    private final ProfileRepository profileRepository;

    @Autowired
    public EmployeeService(EmployeeRepository employeeRepository, ProfileRepository profileRepository) {
        this.employeeRepository = employeeRepository;
        this.profileRepository = profileRepository;
    }

    public Employee createEmployee(Employee theEmployee) {
        if (theEmployee == null) {
            throw new RuntimeException("The body of the entity employee is null");
        }
        return employeeRepository.save(theEmployee);
    }

    public Employee createEmployeeWithProfile(Employee theEmployee, int profileId) {
        Optional<Profile> theProfile = profileRepository.findById(profileId);
        if (theProfile.isEmpty()) {
            throw new ProfileNotFoundException(profileId);
        }

        if (theEmployee == null) {
            throw new RuntimeException("The body of the entity employee is null");
        }

        theEmployee.setProfile(theProfile.get());
        return employeeRepository.save(theEmployee);
    }

    public List<Employee> findAll() {
        return employeeRepository.findAll();
    }

    public Optional<Employee> findById(int theId) {
        Optional<Employee> theEmployee = employeeRepository.findById(theId);

        if (theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException(theId);
        }

        return theEmployee;
    }

    public List<Employee> findEmployeesAboveSalary(double theSalary) {
        return employeeRepository.findEmployeesAboveSalary(theSalary);
    }

    public List<Employee> findByLastName(String lastName) {
        List<Employee> theEmployees = employeeRepository.findByLastName(lastName);

        if (theEmployees.isEmpty()) {
            throw new EmployeeNotFoundException(lastName);
        }

        return theEmployees;
    }

    public List<Employee> findEmployeesByShiftType(ShiftType shiftType) {
        return employeeRepository.findEmployeesByShiftType(shiftType);
    }

    public List<Employee> findEmployeesWithNoSalesTaken() {
        return  employeeRepository.findEmployeesWithNoSalesTaken();
    }

    public Employee updateEmployee(Employee newEmployee, int theId) {
        Optional<Employee> theEmployee = employeeRepository.findById(theId);

        if (theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException(theId);
        }

        Employee dbEmployee = theEmployee.get();
        dbEmployee.setFirstName(newEmployee.getFirstName());
        dbEmployee.setLastName(newEmployee.getLastName());

        return employeeRepository.save(dbEmployee);
    }

    public Employee updateEmployeeWithProfile(Employee newEmployee, int theId, int profileId) {
        Optional<Employee> theEmployee = employeeRepository.findById(theId);
        if (theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException(theId);
        }

        Optional<Profile> theProfile = profileRepository.findById(profileId);
        if (theProfile.isEmpty()) {
            throw new ProfileNotFoundException(theId);
        }

        Employee dbEmployee = theEmployee.get();
        dbEmployee.setFirstName(newEmployee.getFirstName());
        dbEmployee.setLastName(newEmployee.getLastName());
        dbEmployee.setProfile(theProfile.get());

        return employeeRepository.save(dbEmployee);
    }

    public void deleteById(int theId) {
        Optional<Employee> theEmployee = employeeRepository.findById(theId);

        if(theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException(theId);
        }

        employeeRepository.deleteById(theId);
    }

    @Transactional
    public void deleteByShiftTypeSeasonal() {
        employeeRepository.deleteSeasonalEmployees();
    }

    public void deleteAll() {
         employeeRepository.deleteAll();
    }
}
