package com.api.project.service;

import com.api.project.model.Employee;
import com.api.project.model.Profile;
import com.api.project.model.ShiftType;
import com.api.project.repository.EmployeeRepository;
import com.api.project.repository.ProfileRepository;
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
    public void createProfileTest() {
        // arrange
        int employeeId = 1;

        Profile theProfile = new Profile(
                "marian_popa@gmail.com",
                "07441519197",
                "Str. Libertatii, nr. 19, 038919",
                2567.0,
                ShiftType.FULL_TIME,
                LocalDate.of(2000, 2, 17),
                "https://www.linkedin.com/in/marian-popa-223903245/");


        // act
        RuntimeException result = assertThrows(
            RuntimeException.class,
            () -> profileService.create(theProfile, employeeId)
        );

        // assert
        assertEquals(
                "The employee with the id " + employeeId + " was not found in the database",
                result.getMessage(),
                "Should be equals"
        );
    }
}
