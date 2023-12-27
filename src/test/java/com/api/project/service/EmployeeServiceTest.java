package com.api.project.service;

import com.api.project.exception.EmployeeNotFoundException;
import com.api.project.exception.ProfileNotFoundException;
import com.api.project.model.Employee;
import com.api.project.model.Profile;
import com.api.project.model.ShiftType;
import com.api.project.repository.EmployeeRepository;
import com.api.project.repository.ProfileRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("Given an employee that is not null as a parameter, save the employee...")
    public void whenEmployeeIsNotNull_createEmployee_returnEmployee() {
        // arrange
        Employee theEmployee = new Employee("John", "Doe");
        when(employeeRepository.save(theEmployee)).thenReturn(theEmployee);

        // act
        Employee result = employeeService.createEmployee(theEmployee);

        // assert
        assertEquals(theEmployee, result);
    }

    @Test
    @DisplayName("When the employee received as a parameter is null, throw exception...")
    public void whenEmployeeIsNull_createEmployee_throwException() {
        // arrange
        Employee theEmployee = null;

        // act
        RuntimeException result = assertThrows(
                RuntimeException.class,
                () -> employeeService.createEmployee(theEmployee)
        );

        // assert
        assertEquals(
                "The body of the entity employee is null",
                result.getMessage(),
                "Should be equal"
        );
    }

    @Test
    @DisplayName("Save employee and his profile on normal conditions...")
    public void whenEmployeeAndProfileIdAreNotNull_createEmployeeWithProfile_returnEmployee() {
        // arrange
        int profileId = 1;
        Profile theProfile = new Profile();
        theProfile.setProfileId(profileId);

        Employee theEmployee = new Employee("John", "Doe");

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(theProfile));
        when(employeeRepository.save(theEmployee)).thenReturn(theEmployee);

        // act
        Employee result = employeeService.createEmployeeWithProfile(theEmployee, profileId);

        // assert
        assertEquals(theProfile, result.getProfile(), "Should be equal");
    }

    @Test
    @DisplayName("When the profile does not exist, throws exception...")
    public void whenProfileIdDoesNotExist_createEmployeeWithProfile_throwsProfileException() {
        // arrange
        int profileId = 0;
        Employee theEmployee = new Employee("Damian", "Anghel");

        // act
        ProfileNotFoundException result = assertThrows(
                ProfileNotFoundException.class,
                () -> employeeService.createEmployeeWithProfile(theEmployee, profileId)
        );

        // assert
        assertEquals(
                "The profile with the id " + profileId + " was not found in the database",
                result.getMessage(),
                "Should be equal"
        );
    }

    @Test
    @DisplayName("When employee parameter is null, throws exception...")
    public void whenEmployeeIsNull_createEmployeeWithProfile_throwsEmployeeException() {
        // arrange

        // create a random profile
        int profileId = 1;
        Profile theProfile = new Profile();
        theProfile.setProfileId(profileId);
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(theProfile));

        Employee theEmployee = null;

        // act
        RuntimeException result = assertThrows(
                RuntimeException.class,
                () -> employeeService.createEmployeeWithProfile(theEmployee, profileId)
        );

        // assert
        assertEquals(
                "The body of the entity employee is null",
                result.getMessage(),
                "Should be equal"
        );
    }

    @Test
    @DisplayName("Find all the employees and return the result as a list...")
    public void findAllEmployees_returnsListOfEmployees() {
        // arrange
        Employee employee1 = new Employee("John", "Doe");
        Employee employee2 = new Employee("Jane", "Dip");

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);

        when(employeeRepository.findAll()).thenReturn(employees);

        // act
        List<Employee> result = employeeService.findAll();

        // assert
        assertEquals(employees, result, "Should be equals");
    }

    @Test
    @DisplayName("Given an employee id as parameter, return the employee that has that id...")
    public void givenEmployeeId_findById_returnEmployee() {
        // arrange
        Employee theEmployee = new Employee("John", "Doe");
        theEmployee.setEmployeeId(1);
        when(employeeRepository.findById(1)).thenReturn(Optional.of(theEmployee));

        // act
        Optional<Employee> result = employeeService.findById(1);

        // assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(theEmployee.getEmployeeId(), result.get().getEmployeeId(), "Should be equal");
    }

    @Test
    @DisplayName("Given an invalid employee id as parameter, throws exception...")
    public void givenInvalidEmployeeId_findById_throwsException() {
        // arrange

        // act
        RuntimeException result = assertThrows(
                RuntimeException.class,
                () -> employeeService.findById(1)
        );

        // assert
        assertEquals(
                "The employee with the id 1 was not found in the database",
                result.getMessage(),
                "Should be equal"
        );
    }

    @Test
    public void findEmployeesAboveSalary_returnListOfEmployees() {
        // arrange
        Profile profile1 = new Profile();
        profile1.setSalary(2400.0);
        Profile profile2 = new Profile();
        profile2.setSalary(2700.0);
        Profile profile3 = new Profile();
        profile3.setSalary(2750.0);

        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        Employee employee3 = new Employee();

        employee1.setProfile(profile1);
        employee2.setProfile(profile2);
        employee3.setProfile(profile3);

        double theSalary = 2500.0;
        List<Employee> expectedEmployees = List.of(employee2, employee3);

        when(employeeRepository.findEmployeesAboveSalary(theSalary)).thenReturn(expectedEmployees);

        // act
        List<Employee> result = employeeService.findEmployeesAboveSalary(theSalary);

        // Assert
        assertEquals(expectedEmployees.size(), result.size());
        assertEquals(expectedEmployees, result);
        verify(employeeRepository, times(1)).findEmployeesAboveSalary(theSalary);
    }

    @Test
    public void employeesHavingSameLastNameAsParameter_findByLastName_returnEmployeeList() {
        // arrange
        Employee employee1 = new Employee("John", "Doe");
        Employee employee2 = new Employee("Ian", "Smith");
        Employee employee3 = new Employee("Jane", "Doe");

        List<Employee> employees = new ArrayList<>();
        employees.add(employee1);
        employees.add(employee2);
        employees.add(employee3);

        when(employeeRepository.findByLastName("Doe")).thenReturn(employees);

        // act
        List<Employee> result = employeeService.findByLastName("Doe");

        // assert
        assertEquals(employees, result, "Should be equal");
    }

    @Test
    public void findEmployeesByShiftType_returnEmployeeList() {
        // arrange
        ShiftType shiftType = ShiftType.FULL_TIME;

        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        Employee employee3 = new Employee();

        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        Profile profile3 = new Profile();

        employee1.setProfile(profile1);
        employee2.setProfile(profile2);
        employee3.setProfile(profile3);

        profile1.setShiftType(ShiftType.FULL_TIME);
        profile2.setShiftType(ShiftType.FULL_TIME);
        profile3.setShiftType(ShiftType.SEASONAL);

        List<Employee> expectedEmployees = List.of(employee1, employee2);

        when(employeeRepository.findEmployeesByShiftType(shiftType)).thenReturn(expectedEmployees);

        // act
        List<Employee> result = employeeService.findEmployeesByShiftType(shiftType);

        // arrange
        verify(employeeRepository, times(1)).findEmployeesByShiftType(shiftType);
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedEmployees, result);
    }

    @Test
    public void findEmployeesWithNoSalesTaken_returnListOfEmployees() {
        // arrange
        Employee employee1 = new Employee();
        Employee employee2 = new Employee();
        List<Employee> expectedList = List.of(employee1, employee2);

        when(employeeRepository.findEmployeesWithNoSalesTaken()).thenReturn(expectedList);

        // act
        List<Employee> result = employeeService.findEmployeesWithNoSalesTaken();

        // assert
        verify(employeeRepository, times(1)).findEmployeesWithNoSalesTaken();
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(expectedList, result, "Should be equal");
    }

    @Test
    public void whenGivenEmployeeId_updateEmployee_returnEmployee() {
        // arrange
        int theId = 1;
        Employee theEmployee = new Employee("John", "Doe");
        theEmployee.setEmployeeId(theId);

        Employee newEmployee = new Employee("Jean", "Doe");

        when(employeeRepository.findById(theId)).thenReturn(Optional.of(theEmployee));
        when(employeeRepository.save(any(Employee.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // act
        Employee updatedEmployee = employeeService.updateEmployee(newEmployee, theId);

        // assert
        verify(employeeRepository).findById(theId);
        verify(employeeRepository, times(1)).findById(theId);
        verify(employeeRepository, times(1)).save(theEmployee);

        assertEquals(newEmployee.getFirstName(), updatedEmployee.getFirstName());
        assertEquals(newEmployee.getLastName(), updatedEmployee.getLastName());
        assertEquals(theId, updatedEmployee.getEmployeeId());
    }

    @Test
    public void whenEmployeeIdUnavailable_updateEmployee_throwsException() {
        // arrange
        int employeeId = 1;
        Employee theEmployee = new Employee("John", "Doe");
        theEmployee.setEmployeeId(employeeId);

        Employee newEmployee = new Employee("Jean", "Doe");

        // act
        RuntimeException result = assertThrows(
                RuntimeException.class,
                () -> employeeService.updateEmployee(newEmployee, 2)
        );

        // assert
        assertEquals(
                "The employee with the id 2 was not found in the database",
                result.getMessage(),
                "Should be equal"
        );
    }

    @Test
    public void givenNewEmployeeAndProfileId_updateEmployeeWithProfile_returnUpdatedEmployee() {
        // arrange
        int theId = 1;
        int profileId = 2;

        Employee existingEmployee = new Employee("John", "Doe");
        Profile newProfile = new Profile();
        Employee updatedEmployee = new Employee("Jane", "Doe");

        when(employeeRepository.findById(theId)).thenReturn(Optional.of(existingEmployee));
        when(profileRepository.findById(profileId)).thenReturn(Optional.of(newProfile));
        when(employeeRepository.save(existingEmployee)).thenReturn(updatedEmployee);

        // act
        Employee result = employeeService.updateEmployeeWithProfile(new Employee(), theId, profileId);

        // arrange
        verify(employeeRepository, times(1)).findById(theId);
        verify(profileRepository, times(1)).findById(profileId);
        verify(employeeRepository, times(1)).save(existingEmployee);
        assertNotNull(result);
    }

    @Test
    void givenEmployeeIdUnavailable_updateEmployeeWithProfile_throwsEmployeeNotFoundException() {
        // arrange
        int employeeId = 1;
        int profileId = 2;

        Employee employee = new Employee();
        employee.setEmployeeId(2);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.empty());

        EmployeeNotFoundException result = assertThrows(EmployeeNotFoundException.class, () -> {
            employeeService.updateEmployeeWithProfile(new Employee(), employeeId, profileId);
        });

        // assert
        assertEquals(
                "The employee with the id " + employeeId + " was not found in the database",
                result.getMessage(),
                "Should be equal"
        );

        verify(employeeRepository, times(1)).findById(employeeId);
        verify(profileRepository, never()).findById(any());
        verify(employeeRepository, never()).save(any());
    }

    @Test
    void givenProfileIdUnavailable_updateEmployeeWithProfile_throwsProfileNotFoundException() {
        // arrange
        int employeeId = 1;
        int profileId = 1;

        Employee existingEmployee = new Employee();
        existingEmployee.setEmployeeId(employeeId);

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(existingEmployee));
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // act
        ProfileNotFoundException result = assertThrows(ProfileNotFoundException.class, () -> {
            employeeService.updateEmployeeWithProfile(new Employee(), employeeId, profileId);
        });

        // assert
        assertEquals(
                "The profile with the id " + profileId + " was not found in the database",
                result.getMessage(),
                "Should be equal"
        );
        verify(employeeRepository, times(1)).findById(employeeId);
        verify(profileRepository, times(1)).findById(profileId);
        verify(employeeRepository, never()).save(any());
    }

    @Test
    public void deleteByShiftTypeSeasonal_returnsNothing() {
        // arrange
        // act
        employeeService.deleteByShiftTypeSeasonal();

        // assert
        verify(employeeRepository, times(1)).deleteSeasonalEmployees();
    }

    @Test
    public void whenEmployeeIdIsAvailable_deleteById_returnNothing() {
        // arrange
        Employee theEmployee = new Employee("John", "Doe");
        int theId = 1;
        theEmployee.setEmployeeId(theId);

        when(employeeRepository.findById(theId)).thenReturn(Optional.of(theEmployee));

        // act
        employeeService.deleteById(theId);

       // assert
        verify(employeeRepository).deleteById(theId);
    }

    @Test
    public void whenEmployeeIdIsUnavailable_deleteById_throwsException() {
        // arrange

        // act
        RuntimeException result = assertThrows(
                RuntimeException.class,
                () -> employeeService.findById(1)
        );

        // assert
        assertEquals(
                "The employee with the id 1 was not found in the database",
                result.getMessage(),
                "Should be equal"
        );
    }

    @Test
    public void deleteAllEmployees_returnsNothing() {
        // arrange
        // act
        employeeService.deleteAll();

        // assert
        verify(employeeRepository, times(1)).deleteAll();
    }
}
