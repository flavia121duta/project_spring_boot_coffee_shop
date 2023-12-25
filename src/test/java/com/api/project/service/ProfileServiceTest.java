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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProfileRepository profileRepository;

    @Test
    @DisplayName("When the id of the employee received as parameter was not found in the database, then throw a custom exception")
    public void whenEmployeeIdIsWrong_createProfileTest_throwExceptionEmployeeNotFound() {
        // arrange
        int employeeId = 0;

        Profile theProfile = null;

        // act
        RuntimeException result = assertThrows(
            RuntimeException.class,
            () -> profileService.createProfileForEmployee(theProfile, employeeId)
        );

        // assert
        assertEquals(
                "The employee with the id " + employeeId + " was not found in the database",
                result.getMessage(),
                "Should be equals"
        );
    }
}
