package com.api.project.controller;

import com.api.project.dto.ReviewRequest;
import com.api.project.mapper.ReviewMapper;
import com.api.project.model.Review;
import com.api.project.service.ReviewService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
public class ReviewRestController {
    private final ReviewService reviewService;
    private final ReviewMapper reviewMapper;

    @Autowired
    public ReviewRestController(ReviewService reviewService, ReviewMapper reviewMapper) {
        this.reviewService = reviewService;
        this.reviewMapper = reviewMapper;
    }

    @PostMapping
    public ResponseEntity<Review> create(@RequestBody ReviewRequest reviewRequest,
                                         @RequestParam int productId,
                                         @RequestParam int customerId) {
        return ResponseEntity.ok().body(
                reviewService.create(
                        reviewMapper.reviewRequestToReview(reviewRequest),
                        productId,
                        customerId
                ));
    }

    @GetMapping
    public ResponseEntity<List<Review>> findAllReviews() {
        return ResponseEntity.ok().body(reviewService.findAll());
    }

    @GetMapping("/{reviewId}")
    public ResponseEntity<?> findReviewById(@PathVariable int reviewId) {
        return ResponseEntity.ok().body(reviewService.findById(reviewId));
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Review>> findAllReviewOfProduct(@PathVariable int productId) {
        return ResponseEntity.ok().body(reviewService.findAllReviewOfProduct(productId));
    }
}
