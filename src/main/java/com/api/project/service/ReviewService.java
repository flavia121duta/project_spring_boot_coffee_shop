package com.api.project.service;

import com.api.project.exception.CustomerNotFoundException;
import com.api.project.exception.ProductNotFoundException;
import com.api.project.exception.ReviewNotFoundException;
import com.api.project.model.Customer;
import com.api.project.model.Product;
import com.api.project.model.Review;
import com.api.project.repository.CustomerRepository;
import com.api.project.repository.ProductRepository;
import com.api.project.repository.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class ReviewService {
    private final ReviewRepository reviewRepository;

    private final CustomerRepository customerRepository;

    private final ProductRepository productRepository;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, CustomerRepository customerRepository, ProductRepository productRepository) {
        this.reviewRepository = reviewRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public Review create(Review theReview, int productId, int customerId) {
        Optional<Product> theProduct = productRepository.findById(productId);
        if(theProduct.isEmpty()) {
            throw new ProductNotFoundException("The product with the id " + productId + " was not found");
        }

        Optional<Customer> theCustomer = customerRepository.findById(customerId);
        if(theCustomer.isEmpty()) {
            throw new CustomerNotFoundException("The customer with the id " + customerId + " was not found");
        }

        theReview.setCustomer(theCustomer.get());
        theReview.setProduct(theProduct.get());
        return reviewRepository.save(theReview);
    }

    public List<Review> findAll() {
        return reviewRepository.findAll();
    }

    public Optional<Review> findById(int theId) {
        Optional<Review> theReview = reviewRepository.findById(theId);

        if (theReview.isEmpty()) {
            throw new ReviewNotFoundException("The review with the id " + theId + " was not found");
        }

        return theReview;
    }

    public List<Review> findAllReviewOfProduct(int productId) {
        Optional<Product> theProduct = productRepository.findById(productId);
        if(theProduct.isEmpty()) {
            throw new ProductNotFoundException("The product with teh id " + productId + " was not found");
        }

        List<Review> allReviews = reviewRepository.findAll();

        List<Review> theReviewsOfTheProduct = new ArrayList<>();
        for(Review theReview: allReviews) {
            if (theReview.getProduct().getProductId() == productId) {
                theReviewsOfTheProduct.add(theReview);
            }
        }

        return theReviewsOfTheProduct;
    }
}
