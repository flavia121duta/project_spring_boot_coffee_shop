package com.api.project.repository;

import com.api.project.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CustomerRepository extends JpaRepository<Customer, Integer> {
    @Query("SELECT c from Customer c JOIN Review r ON c.customerId = r.customer.customerId WHERE r.product.productId = :productId")
    List<Customer> getCustomersWhoSubmitterReviewsToProductByProductId(int productId);
}
