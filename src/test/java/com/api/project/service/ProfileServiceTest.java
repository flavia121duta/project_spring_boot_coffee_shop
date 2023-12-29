package com.api.project.service;

import com.api.project.exception.ProfileNotFoundException;
import com.api.project.model.Employee;
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
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

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
        // arrange
        Profile profile = new Profile();
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        // act
        Profile savedProfile = profileService.createProfile(profile);

        // assert
        assertNotNull(savedProfile);
        assertEquals(savedProfile, profile, "The objects should match");
    }

    @Test
    public void givenNullProfile_createProfile_NoOperationPerformed() {
        // arrange
        Profile profile = null;

        // act
        Optional<Profile> result = Optional.ofNullable(profileService.createProfile(profile));

        // assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
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
    public void givenAvailableProfileId_findById_returnsProfile() {
        // arrange
        int profileId = 1;
        Profile profile  = new Profile();
        profile.setProfileId(profileId);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(profile));

        // act
        Profile result = profileService.findById(profileId);

        // assert
        assertThat(result).isNotNull();
        assertEquals(result, profile, "Should be equal");
    }

    @Test
    public void givenUnavailableProfileId_findById_throwProfileNotFoundException() {
        // arrange
        when(profileRepository.findById(1)).thenReturn(Optional.empty());

        // act
        ProfileNotFoundException result = assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.findById(1)
        );

        // assert
        assertNotNull(result);
        assertEquals(result.getMessage(), "The profile with the id 1 was not found in the database", "The messages should match");
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
    public void givenProfileObjectAndEmployeeId_createProfileForEmployee_returnProfile() {
        // arrange
        int employeeId = 1;
        Employee employee = new Employee();
        Profile profile = new Profile();

        when(employeeRepository.findById(employeeId)).thenReturn(Optional.of(employee));
        when(profileRepository.save(profile)).thenReturn(profile);

        // act
        Profile result = profileService.createProfileForEmployee(profile, employeeId);

        // assert
        assertNotNull(result);
        assertEquals(result.getEmployee(), employee, "The employee found in the database should match the employee set for the saved profile");
        assertEquals(profile, result, "The two profiles should match");

        verify(employeeRepository, times(1)).findById(eq(employeeId));
        verify(profileRepository, times(1)).save(eq(profile));
    }

    @Test
    @DisplayName("When the id of the employee received as parameter was not found in the database, then throw a custom exception as EmployeeNotFoundException")
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

    @Test
    public void givenNewProfileObjectAndProfileIdAvailable_updateProfile_returnNewProfile() {
        // arrange
        int profileId = 1;
        Profile existingProfile = new Profile(
                "john_doe@gmail.com",
                "07552223333",
                "Str. Unirii, nr. 14, 031459",
                2500.0,
                ShiftType.SEASONAL,
                LocalDate.of(19999, 10, 28),
                "linkedin.com"
        );
        existingProfile.setProfileId(profileId);

        Profile newProfile = existingProfile;
        newProfile.setSalary(2600.0);

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(existingProfile));
        when(profileRepository.save(any(Profile.class))).thenReturn(newProfile);

        // act
        Profile result = profileService.update(newProfile, profileId);

        // assert
        assertNotNull(result);
        assertEquals(newProfile.getSalary(), result.getSalary());
        assertEquals(profileId, result.getProfileId());
        assertEquals(newProfile, result);

        verify(profileRepository, times(1)).findById(eq(profileId));
        verify(profileRepository, times(1)).save(eq(existingProfile));
    }

    @Test
    public void givenNewProfileObjectAndProfileIdUnavailable_updateProfile_throwProfileNotFoundException() {
        // arrange
        int profileId = 1;
        Profile existingProfile = new Profile();
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // act
        ProfileNotFoundException result = assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.update(existingProfile, profileId)
        );

        // assert
        assertEquals(result.getMessage(), "The profile with the id " + profileId + " was not found in the database");
        verify(profileRepository, never()).save(any());
    }

    @Test
    public void givenAvailableProfileId_deleteProfileById_returnsNothing() {
        // arrange
        int profileId = 1;
        Profile existingProfile = new Profile();
        existingProfile.setEmployee(new Employee());

        when(profileRepository.findById(profileId)).thenReturn(Optional.of(existingProfile));

        // act
        profileService.deleteById(profileId);

        // assert
        verify(profileRepository).deleteById(profileId);
    }

    @Test
    public void givenUnavailableProfileId_throwsProfileNotFoundException() {
        // arrange
        int profileId = 1;
        Profile existingProfile = new Profile();
        when(profileRepository.findById(profileId)).thenReturn(Optional.empty());

        // act
        ProfileNotFoundException result = assertThrows(
                ProfileNotFoundException.class,
                () -> profileService.deleteById(profileId)
        );

        // assert
        assertEquals(result.getMessage(), "The profile with the id " + profileId + " was not found in the database");
        verify(profileRepository, never()).save(any());
    }

    @Test
    public void deleteAll_returnsNothing() {
        // arrange
        Profile profile1 = new Profile();
        Profile profile2 = new Profile();
        Profile profile3 = new Profile();

        // act
        profileService.deleteAll();

        // assert
        verify(profileRepository, times(1)).deleteAll();
    }
}
