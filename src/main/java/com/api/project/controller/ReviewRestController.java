package com.api.project.controller;

import com.api.project.dto.ReviewRequest;
import com.api.project.mapper.ReviewMapper;
import com.api.project.model.Review;
import com.api.project.service.ReviewService;
import jakarta.validation.Valid;
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

    @PutMapping("/{reviewId}")
    public ResponseEntity<Review> updateReview(@RequestBody @Valid ReviewRequest reviewRequest,
                                                   @PathVariable int reviewId) {
        return ResponseEntity.ok().body(
                reviewService.update(reviewMapper.reviewRequestToReview(reviewRequest), reviewId)
        );

    }

    @DeleteMapping("/{reviewId}")
    public String deleteReview(@PathVariable int reviewId) {
        reviewService.deleteById(reviewId);
        return "The review with the id: " + reviewId + " was successfully deleted.";
    }

    @DeleteMapping
    public String deleteAllReviews() {
        int numberOfReviews = reviewService.findAll().size();
        reviewService.deleteAll();
        return "All " + numberOfReviews + " customers were successfully deleted from the database.";
    }
}
