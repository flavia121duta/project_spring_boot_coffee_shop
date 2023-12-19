package com.api.project.service;

import com.api.project.exception.EmployeeNotFoundException;
import com.api.project.exception.ProductNotFoundException;
import com.api.project.exception.ProfileNotFoundException;
import com.api.project.model.Employee;
import com.api.project.model.Profile;
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

    public Employee create(Employee theEmployee) {
        return employeeRepository.save(theEmployee);
    }

    public Employee create(Employee theEmployee, int profileId) {
        Optional<Profile> theProfile = profileRepository.findById(profileId);
        if (theProfile.isEmpty()) {
            throw new ProfileNotFoundException("The profile with the id " + profileId + " was not found");
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
            throw new EmployeeNotFoundException("The employee with the id: " + theId + " was not found");
        }

        return theEmployee;
    }

    public List<Employee> findEmployeesAboveSalary(double theSalary) {
        return employeeRepository.findEmployeesAboveSalary(theSalary);
    }

    public Employee update(Employee newEmployee, int theId) {
        Optional<Employee> theEmployee = employeeRepository.findById(theId);

        if (theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException("The employee with the id: " + theId + " was not found");
        }

        Employee dbEmployee = theEmployee.get();
        dbEmployee.setFirstName(newEmployee.getFirstName());
        dbEmployee.setLastName(newEmployee.getLastName());

        return employeeRepository.save(dbEmployee);
    }

    public Employee update(Employee newEmployee, int theId, int profileId) {
        Optional<Employee> theEmployee = employeeRepository.findById(theId);
        Optional<Profile> theProfile = profileRepository.findById(profileId);

        if (theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException("The employee with the id " + theId + " was not found");
        }

        if (theProfile.isEmpty()) {
            throw new ProductNotFoundException("The profile with the id " + theId + " was not found");
        }

        Employee dbEmployee = theEmployee.get();
        dbEmployee.setFirstName(newEmployee.getFirstName());
        dbEmployee.setLastName(newEmployee.getLastName());
        dbEmployee.setProfile(theProfile.get());

        return employeeRepository.save(dbEmployee);
    }

    public List<Employee> findByLastName(String lastName) {
        List<Employee> theEmployees = employeeRepository.findByLastName(lastName);

        if (theEmployees.isEmpty()) {
            throw new EmployeeNotFoundException("No employee has the last name: " + lastName + " found in the database");
        }

        return theEmployees;
    }

    public void deleteById(int theId) {
        Optional<Employee> theEmployee = employeeRepository.findById(theId);

        if(theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException("The employee with the id: " + theId + " was not found. " +
                    "No delete performed on the database");
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
