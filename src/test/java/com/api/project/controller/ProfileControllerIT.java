package com.api.project.controller;

import com.api.project.dto.ProfileRequest;
import com.api.project.mapper.ProfileMapper;
import com.api.project.model.Profile;
import com.api.project.model.ShiftType;
import com.api.project.service.ProfileService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@WebMvcTest(controllers = ProfileRestController.class)
class ProfileControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ProfileService profileService;

    @MockBean
    private ProfileMapper profileMapper;

    @Test
    public void createProfile() throws Exception {
        // arrange
        Profile profile = new Profile(
                "john_doe@gmail.com",
                "0755222333",
                "Str. Unirii, nr. 14, 031459",
                2500.0,
                ShiftType.SEASONAL,
                null,
                "linkedin.com"
        );
        ProfileRequest profileRequest = new ProfileRequest(
                "john_doe@gmail.com",
                "0755222333",
                "Str. Unirii, nr. 14, 031459",
                2500.0,
                ShiftType.SEASONAL,
                null,
                "linkedin.com"
        );

        when(profileMapper.profileRequestToProfile(profileRequest)).thenReturn(profile);
        when(profileService.createProfile(profile)).thenReturn(profile);

        // act & assert
        mockMvc.perform(post("/profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void createProfileForExistingEmployee() throws Exception {
        // arrange
        ProfileRequest profileRequest = new ProfileRequest(
                "john_doe@gmail.com",
                "0755222333",
                "Str. Unirii, nr. 14, 031459",
                2500.0,
                ShiftType.SEASONAL,
                null,
                "linkedin.com"
        );
        Profile createdProfile = new Profile(
                "john_doe@gmail.com",
                "0755222333",
                "Str. Unirii, nr. 14, 031459",
                2500.0,
                ShiftType.SEASONAL,
                null,
                "linkedin.com"
        );
        int employeeId = 12;

        when(profileMapper.profileRequestToProfile(profileRequest)).thenReturn(createdProfile);
        when(profileService.createProfileForEmployee(createdProfile, employeeId)).thenReturn(createdProfile);

        // act & assert
        mockMvc.perform(post("/profiles")
                        .param("employeeId", String.valueOf(employeeId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void getAverageSalary() throws Exception {
        // arrange
        double expectedAverage = 2500.0;

        when(profileService.getAverageSalary()).thenReturn(expectedAverage);

        // act & assert
        mockMvc.perform(get("/profiles/average-salary"))
                .andExpect(status().isOk())
                .andExpect(content().string(String.valueOf(expectedAverage)));
    }

    @Test
    public void updateExistingProfile() throws Exception {
        // arrange
        ProfileRequest profileRequest = new ProfileRequest(
                "john_doe@gmail.com",
                "0755222333",
                "Str. Unirii, nr. 14, 031459",
                2500.0,
                ShiftType.SEASONAL,
                null,
                "linkedin.com"
        );
        int profileId = 1;

        Profile updatedProfile = new Profile();

        when(profileMapper.profileRequestToProfile(profileRequest)).thenReturn(updatedProfile);
        when(profileService.update(updatedProfile, profileId)).thenReturn(updatedProfile);

        // Act & Assert
        mockMvc.perform(put("/profiles/{profileId}", profileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().is2xxSuccessful());
    }

    @Test
    public void updateNonexistentProfile() throws Exception {
        // arrange
        ProfileRequest profileRequest = new ProfileRequest(
                "john_doe@gmail.com",
                "0755222333",
                "Str. Unirii, nr. 14, 031459",
                2500.0,
                ShiftType.SEASONAL,
                null,
                "linkedin.com"
        );
        int nonExistingProfileId = 999;

        when(profileMapper.profileRequestToProfile(profileRequest)).thenReturn(new Profile());

        // Act & Assert
        mockMvc.perform(put("/profiles/{profileId}", nonExistingProfileId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(profileRequest)))
                .andExpect(status().isOk());
    }
}