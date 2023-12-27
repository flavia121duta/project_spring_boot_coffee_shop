package com.api.project.service;

import com.api.project.model.Profile;
import com.api.project.model.ShiftType;
import com.api.project.repository.EmployeeRepository;
import com.api.project.repository.ProfileRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ProfileServiceTest {
    @InjectMocks
    private ProfileService profileService;

    @Mock
    private EmployeeRepository employeeRepository;

    @Mock
    private ProfileRepository profileRepository;

    @BeforeEach
    public void setup() {
        Profile profile1 = new Profile(
                "john_doe@gmail.com",
                "07552223333",
                "Str. Unirii, nr. 14, 031459",
                2500.0,
                ShiftType.SEASONAL,
                LocalDate.of(19999, 10, 28),
                "linkedin.com"
                );

        Profile profile2 = new Profile(
                "jane_doe@gmail.com",
                "07662223333",
                "Str. Unirest, nr. 14, 031459",
                2700.0,
                ShiftType.FULL_TIME,
                LocalDate.of(19999, 9, 20),
                "linkedin.com"
        );
    }

    @Test
    public void givenProfileObject_createProfile_returnsTheProfile() {
        Profile profile = new Profile();
        when(profileRepository.save(Mockito.any(Profile.class))).thenReturn(profile);

        Profile savedProfile = profileService.createProfile(profile);

        assertNotNull(savedProfile);
        System.out.println(savedProfile);
    }

    @Test
    public void findAllProfiles_returnsListProfiles() {
        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        when(profileRepository.findAll()).thenReturn(List.of(profile1, profile2));

        List<Profile> profileList = profileService.findAll();

        assertThat(profileList).isNotNull();
        assertThat(profileList.size()).isEqualTo(2);
    }

    @Test
    public void getAverageSalary_returnsDoubleValue() {
        double expectedAverageSalary = 2600.0;
        when(profileRepository.getAverageSalary()).thenReturn(expectedAverageSalary);

        double actualAverageSalary = profileService.getAverageSalary();

        assertThat(actualAverageSalary).isGreaterThan(0);
        assertEquals(expectedAverageSalary, actualAverageSalary, "Should be equal");
    }

    @Test
    @DisplayName("When the id of the employee received as parameter was not found in the database, then throw a custom exception")
    public void whenEmployeeIdIsNotFound_createProfileTest_throwExceptionEmployeeNotFound() {
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
