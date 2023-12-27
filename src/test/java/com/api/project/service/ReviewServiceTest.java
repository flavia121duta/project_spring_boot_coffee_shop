package com.api.project.service;

import com.api.project.exception.CustomerNotFoundException;
import com.api.project.exception.ProductNotFoundException;
import com.api.project.model.Customer;
import com.api.project.model.Product;
import com.api.project.model.Profile;
import com.api.project.model.Review;
import com.api.project.repository.CustomerRepository;
import com.api.project.repository.ProductRepository;
import com.api.project.repository.ReviewRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.verifyNoInteractions;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {
    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CustomerRepository customerRepository;

    @Test
    public void whenProductAndCustomerExists_createReview_savesReview() {
        // arrange
        Review review = new Review();
        Product product = new Product();
        Customer customer = new Customer();
        review.setProduct(product);
        review.setCustomer(customer);

        Optional<Product> productOptional = Optional.of(product);
        when(productRepository.findById(product.getProductId())).thenReturn(productOptional);
        Optional<Customer> customerOptional = Optional.of(customer);
        when(customerRepository.findById(customer.getCustomerId())).thenReturn(customerOptional);

        Review savedReview = new Review();
        savedReview.setReviewId(1);
        when(reviewRepository.save(review)).thenReturn(savedReview);

        // act
        Review result = reviewService.create(review, productOptional.get().getProductId(), customerOptional.get().getCustomerId());

        // assert
        assertNotNull(result);
        assertEquals(savedReview.getReviewId(), result.getReviewId());
        verify(productRepository).findById(product.getProductId());
        verify(customerRepository).findById(customer.getCustomerId());
        verify(reviewRepository).save(review);
    }

    @Test
    void whenProductDoesNotExist_createReview_throwsProductNotFoundException() {
        // Arrange
        Review review = new Review();
        Product product = new Product();
        Customer customer = new Customer();
        review.setProduct(product);
        review.setCustomer(customer);

        when(productRepository.findById(product.getProductId()))
                .thenReturn(Optional.empty());
        when(customerRepository.findById(customer.getCustomerId()))
                .thenReturn(Optional.of(customer));
        // Act
        ProductNotFoundException exception = assertThrows(ProductNotFoundException.class,
                () -> reviewService.create(review, product.getProductId(), customer.getCustomerId()));

        // Assert
        assertEquals("The product with the id " + product.getProductId() + " was not found in the database",
                exception.getMessage());

        verify(productRepository).findById(product.getProductId());
        verify(reviewRepository, times(0)).save(review);
        verifyNoInteractions(reviewRepository);
    }

    @Test
    void whenCustomerDoesNotExist_createReview_throwsCustomerNotFoundException() {
        // Arrange
        Review review = new Review();
        Product product = new Product();
        Customer customer = new Customer();
        review.setProduct(product);
        review.setCustomer(customer);

        when(productRepository.findById(product.getProductId())).
                thenReturn(Optional.of(product));
        when(customerRepository.findById(customer.getCustomerId()))
                .thenReturn(Optional.empty());

        // Act
        CustomerNotFoundException exception = assertThrows(CustomerNotFoundException.class,
                () -> reviewService.create(review, product.getProductId(), customer.getCustomerId()));

        // Assert
        assertEquals("The customer with the id " + customer.getCustomerId() + " was not found in the database",
                exception.getMessage());

        verify(customerRepository).findById(customer.getCustomerId());
        verify(reviewRepository, times(0)).save(review);
        verifyNoInteractions(reviewRepository);
    }

    @Test
    public void findAllReviews_returnListReviews() {
        Review review1 = new Review();
        Review review2 = new Review();
        Review review3 = new Review();

        when(reviewRepository.findAll()).thenReturn(List.of(review1, review2, review3));

        List<Review> reviewList = reviewService.findAll();

        assertThat(reviewList).isNotNull();
        assertThat(reviewList.size()).isEqualTo(3);
    }

    @Test
    public void givenValidProductId_findAllReviewOfProduct_returnsListReviews() {
        int productId = 1;
        Product product = new Product();
        product.setProductId(productId);

        Review review1 = new Review();
        Review review2 = new Review();
        review1.setProduct(product);
        review2.setProduct(product);

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(reviewRepository.findAll()).thenReturn(List.of(review1, review2));

        List<Review> reviewList = reviewService.findAllReviewOfProduct(productId);

        assertThat(reviewList).isNotNull();
        assertThat(reviewList.size()).isEqualTo(2);
    }

}