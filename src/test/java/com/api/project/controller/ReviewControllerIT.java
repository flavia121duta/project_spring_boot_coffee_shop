package com.api.project.controller;

import com.api.project.mapper.ReviewMapper;
import com.api.project.model.Review;
import com.api.project.service.ReviewService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = ReviewRestController.class)
class ReviewControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ReviewService reviewService;

    @MockBean
    private ReviewMapper reviewMapper;

    @Test
    public void findAllReviewOfProductByProductId() throws Exception {
        // arrange
        int productId = 1;

        Review review1 = new Review();
        Review review2 = new Review();

        List<Review> expectedReviews = List.of(review1, review2);

        when(reviewService.findAllReviewOfProduct(productId)).thenReturn(expectedReviews);

        // act & assert
        mockMvc.perform(get("/reviews/product/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedReviews)));
    }
}