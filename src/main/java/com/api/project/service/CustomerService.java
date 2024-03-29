package com.api.project.service;

import com.api.project.exception.CustomerNotFoundException;
import com.api.project.exception.ProductNotFoundException;
import com.api.project.model.Customer;
import com.api.project.model.Product;
import com.api.project.model.Review;
import com.api.project.repository.CustomerRepository;
import com.api.project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository, ProductRepository productRepository) {
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
    }

    public Customer create(Customer theCustomer) {
        return customerRepository.save(theCustomer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(int theId) {
        Optional<Customer> theCustomer = customerRepository.findById(theId);

        if (theCustomer.isEmpty()) {
            throw new CustomerNotFoundException(theId);
        }

        return theCustomer;
    }

    public List<Customer> getCustomersWhoSubmitterReviewsToProductByProductId(int productId) {
        Optional<Product> theProduct = productRepository.findById(productId);
        if (theProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }

        return customerRepository.getCustomersWhoSubmitterReviewsToProductByProductId(productId);
    }

    public Customer update(Customer newCustomer, int theId) {
        Optional<Customer> theCustomer = customerRepository.findById(theId);

        if (theCustomer.isEmpty()) {
            throw new CustomerNotFoundException(theId);
        }

        Customer dbCustomer = theCustomer.get();
        dbCustomer.setEmail(newCustomer.getEmail());
        dbCustomer.setFullName(newCustomer.getFullName());
        dbCustomer.setReviews(newCustomer.getReviews());

        return customerRepository.save(dbCustomer);
    }

    public void deleteById(int theId) {
        Optional<Customer> theCustomer = customerRepository.findById(theId);

        if(theCustomer.isEmpty()) {
            throw new CustomerNotFoundException(theId);
        }

        List<Review> reviewsGivenByCustomer = theCustomer.get().getReviews();
        for(Review review: reviewsGivenByCustomer) {
            review.setCustomer(null);
        }

        customerRepository.deleteById(theId);
    }

    public void deleteAll() {
        customerRepository.deleteAll();
    }
}
