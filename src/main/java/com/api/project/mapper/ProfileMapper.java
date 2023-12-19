package com.api.project.mapper;

import com.api.project.dto.ProfileRequest;
import com.api.project.model.Profile;
import org.springframework.stereotype.Component;

@Component
public class ProfileMapper {
    public Profile profileRequestToProfile(ProfileRequest profileRequest) {
        return new Profile(profileRequest.getEmail(),
                profileRequest.getPhone(),
                profileRequest.getAddress(),
                profileRequest.getSalary(),
                profileRequest.getShiftType(),
                profileRequest.getDateOfBirth(),
                profileRequest.getLinkedinProfile());
    }
}
