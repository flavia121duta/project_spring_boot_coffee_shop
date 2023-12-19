package com.api.project.controller;

import com.api.project.dto.ProfileRequest;
import com.api.project.mapper.ProfileMapper;
import com.api.project.model.Profile;
import com.api.project.service.ProfileService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/profiles")
public class ProfileRestController {
    private final ProfileService profileService;

    private final ProfileMapper profileMapper;

    @Autowired
    public ProfileRestController(ProfileService profileService, ProfileMapper profileMapper) {
        this.profileService = profileService;
        this.profileMapper = profileMapper;
    }

    @PostMapping
    public ResponseEntity<Profile> create(@RequestBody @Valid ProfileRequest theProfile, @RequestParam(required = false) Integer employeeId) {
        if (employeeId == null) {
            return ResponseEntity
                    .ok()
                    .body(profileService.create(profileMapper.profileRequestToProfile(theProfile)));
        }

        return ResponseEntity
                .ok()
                .body(profileService.create(profileMapper.profileRequestToProfile(theProfile), employeeId));
    }

    @GetMapping
    public ResponseEntity<List<Profile>> findAll() {
        return ResponseEntity.ok().body(profileService.findAll());
    }

    @GetMapping("/{profileId}")
    public ResponseEntity<?> getProfileById(@PathVariable int profileId) {
        return ResponseEntity.ok().body(profileService.findById(profileId));
    }

    @GetMapping("/average-salary")
    public double getAverageSalary() {
        return profileService.gentAverageSalary();
    }

    @PutMapping("/{profileId}")
    public ResponseEntity<Profile> updateProfile(@RequestBody @Valid ProfileRequest theProfile, @PathVariable int profileId) {
        return ResponseEntity.ok().body(
                profileService.update(
                        profileMapper.profileRequestToProfile(theProfile),
                        profileId
                )
        );
    }

    @DeleteMapping("/{profileId}")
    public String deleteProfile(@PathVariable int profileId) {
        profileService.deleteById(profileId);
        return "The profile with the id: " + profileId + " was successfully deleted.";
    }

    @DeleteMapping
    public String deleteAllProfiles() {
        int numberOfProfiles = profileService.findAll().size();
        profileService.deleteAll();
        return "All " + numberOfProfiles + " profiles were successfully deleted from the database.";
    }
}
