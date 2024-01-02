package com.api.project.service;

import com.api.project.exception.ProductNotFoundException;
import com.api.project.model.Customer;
import com.api.project.model.Product;
import com.api.project.repository.CustomerRepository;
import com.api.project.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CustomerServiceTest {
    @InjectMocks
    private CustomerService customerService;

    @Mock
    private CustomerRepository customerRepository;

    @Mock
    private ProductRepository productRepository;

    @Test
    public void givenExistingProductId_getCustomersWhoSubmitterReviewsToProductByProductId_returnListOfCustomers() {
        // arrange
        int productId = 1;
        Product product = new Product();
        product.setProductId(productId);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Customer customer1 = new Customer();
        Customer customer2 = new Customer();

        List<Customer> expectedList = List.of(customer1, customer2);

        when(customerRepository.getCustomersWhoSubmitterReviewsToProductByProductId(productId)).thenReturn(expectedList);

        // act
        List<Customer> actualList = customerService.getCustomersWhoSubmitterReviewsToProductByProductId(productId);

        // assert
        assertNotNull(actualList);
        assertEquals(expectedList, actualList, "Should be equal");
    }

    @Test
    public void givenNonexistentProductId_getCustomersWhoSubmitterReviewsToProductByProductId_throwsProductNotFoundException() {
        // arrange
        int productId = 100;
        when(productRepository.findById(productId)).thenReturn(Optional.empty());

        // act
        ProductNotFoundException result = assertThrows(
                ProductNotFoundException.class,
                () -> customerService.getCustomersWhoSubmitterReviewsToProductByProductId(productId)
        );

        // assert
        assertNotNull(result);
        assertEquals("The product with the id " + productId + " was not found in the database", result.getMessage(), "Should be equal");
    }
}