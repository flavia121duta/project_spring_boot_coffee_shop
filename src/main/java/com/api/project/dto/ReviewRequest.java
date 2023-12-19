package com.api.project.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public class ReviewRequest {
    @Min(1)
    @Max(5)
    private double stars;

    @NotNull
    private String note;

    public ReviewRequest() {
    }

    public ReviewRequest(double stars, String note) {
        this.stars = stars;
        this.note = note;
    }

    public double getStars() {
        return stars;
    }

    public void setStars(double stars) {
        this.stars = stars;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
