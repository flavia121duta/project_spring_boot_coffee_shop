package com.api.project.service;

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

import java.time.LocalDate;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class EmployeeServiceTest {

    @InjectMocks
    private EmployeeService employeeService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("Running save employee behaviour on happy flow...")
    public void saveEmployeeTest() {
        // arrange
        Employee theEmployee = new Employee("Ionut", "Popescu");
        when(employeeRepository.save(theEmployee)).thenReturn(theEmployee);

        // act
        Employee result = employeeService.create(theEmployee);

        // assert
        assertEquals(theEmployee, result);
    }

    @Test
    @DisplayName("Running save employee and his profile...")
    public void saveEmployeeAndEmployeeProfileTest() {
        // arrange
        int profileId = 1;
        Profile theProfile = new Profile(
                "ionut_popescu@gmail.com",
                "07331519197",
                "Str. Pacii, nr. 16, 045916",
                2867.0,
                ShiftType.FULL_TIME,
                LocalDate.of(2000, 3, 16),
                "https://www.linkedin.com/in/ionut-popescu-223903245/");

        Employee theEmployee = new Employee("Ionut", "Popescu");

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(theProfile));
        when(employeeRepository.save(theEmployee)).thenReturn(theEmployee);

        // act
        Employee result = employeeService.create(theEmployee, profileId);

        // assert
        assertEquals(theProfile, result.getProfile(), "Should be equals");
    }
}
