package com.api.project.service;

import com.api.project.exception.EmployeeNotFoundException;
import com.api.project.exception.ProfileNotFoundException;
import com.api.project.model.Employee;
import com.api.project.model.Profile;
import com.api.project.repository.EmployeeRepository;
import com.api.project.repository.ProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProfileService {
    private final ProfileRepository profileRepository;
    private final EmployeeRepository employeeRepository;

    @Autowired
    public ProfileService(ProfileRepository profileRepository, EmployeeRepository employeeRepository) {
        this.profileRepository = profileRepository;
        this.employeeRepository = employeeRepository;
    }

    public List<Profile> findAll() {
        return profileRepository.findAll();
    }

    public Profile findById(int theId) {
        return profileRepository.findById(theId).orElseThrow(() -> new ProfileNotFoundException(theId));
    }

    public double getAverageSalary() {
        return profileRepository.getAverageSalary();
    }

    public Profile createProfile(Profile theProfile) {
        return  profileRepository.save(theProfile);
    }

    public Profile createProfileForEmployee(Profile theProfile, int employeeId) {
        Optional<Employee> theEmployee = employeeRepository.findById(employeeId);
        if (theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException(employeeId);
        }

        theProfile.setEmployee(theEmployee.get());
        return profileRepository.save(theProfile);
    }

    public Profile update(Profile newProfile, int profileId) {
        Optional<Profile> theProfile = profileRepository.findById(profileId);

        if (theProfile.isEmpty()) {
            throw new ProfileNotFoundException(profileId);
        }

        Profile dbProfile = theProfile.get();

        dbProfile.setEmail(newProfile.getEmail());
        dbProfile.setPhone(newProfile.getPhone());
        dbProfile.setAddress(newProfile.getAddress());
        dbProfile.setSalary(newProfile.getSalary());
        dbProfile.setShiftType(newProfile.getShiftType());
        dbProfile.setDateOfBirth(newProfile.getDateOfBirth());
        dbProfile.setLinkedinProfile(newProfile.getLinkedinProfile());

        return profileRepository.save(dbProfile);
    }

    public void deleteById(int theId) {
        Optional<Profile> theProfile = profileRepository.findById(theId);

        if (theProfile.isEmpty()) {
            throw new ProfileNotFoundException(theId);
        }

        Profile profile = theProfile.get();
        profile.getEmployee().setProfile(null);

        profileRepository.deleteById(theId);

    }

    public void deleteAll() {
        profileRepository.deleteAll();
    }
}
