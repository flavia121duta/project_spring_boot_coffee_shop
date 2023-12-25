package com.api.project.exception;

public class ReviewNotFoundException extends NotFoundException {
    public ReviewNotFoundException(int reviewId) {
        super("The review with the id " + reviewId + " was not found in the database");
    }
}
