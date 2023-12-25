package com.api.project.exception;

public class ProfileNotFoundException extends NotFoundException {
    public ProfileNotFoundException(int profileId) {
        super("The profile with the id " + profileId + " was not found in the database");
    }
}
