package com.api.project.mapper;

import com.api.project.dto.ReviewRequest;
import com.api.project.model.Review;
import org.springframework.stereotype.Component;

@Component
public class ReviewMapper {
    public Review reviewRequestToReview(ReviewRequest reviewRequest) {
        return new Review(reviewRequest.getStars(), reviewRequest.getNote());
    }
}
