package com.api.project.service;

import com.api.project.exception.DuplicateProductException;
import com.api.project.model.Product;
import com.api.project.model.ProductType;
import com.api.project.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {
    @InjectMocks
    private ProductService productService;


    @Mock
    private ProductRepository productRepository;

    @Test
    void whenProductDoesNotExist_create_saveTheProduct() {
        // arrange
        Product product = new Product("Ice Cream", 7.8, ProductType.OTHERS);
        when(productRepository.findByProductName(product.getProductName())).thenReturn(Optional.empty());

        Product savedProduct = new Product("Ice Cream", 8.0, ProductType.OTHERS);
        savedProduct.setProductId(1);
        when(productRepository.save(product)).thenReturn(savedProduct);

        // act
        Product result = productService.create(product);

        // assert
        assertNotNull(result);
        assertEquals(savedProduct.getProductId(), result.getProductId());
        assertEquals(savedProduct.getProductName(), result.getProductName());
        assertEquals(product.getProductName(), result.getProductName());
        verify(productRepository).findByProductName(product.getProductName());
        verify(productRepository).save(product);
    }

    @Test
    public void whenProductAlreadyExists_create_throwsDuplicateProductException() {
        // arrange
        Product product = new Product("Chocolate Croissants", 10.5, ProductType.BAKERY);
        when(productRepository.findByProductName(product.getProductName())).thenReturn(Optional.of(product));

        // act
        DuplicateProductException exception = assertThrows(
                DuplicateProductException.class,
                () -> productService.create(product)
        );

        // assert
        assertEquals("A product with the same name \"" + product.getProductName() + " \"already exists.", exception.getMessage());
        verify(productRepository, times(0)).save(product);
    }

    @Test
    public void whenProductExists_findById_returnsTheProduct() {
        // arrange
        Product product = new Product("Strawberry Doughnut", 8.2, ProductType.BAKERY);
        product.setProductId(1);
        when(productRepository.findById(1)).thenReturn(Optional.of(product));

        // act
        Optional<Product> result = productService.findById(1);

        // assert
        assertNotNull(result);
        assertTrue(result.isPresent());
        assertEquals(product.getProductId(), result.get().getProductId(), "The ids should match");
        assertEquals(product, result.get(), "Should be equal");
    }

    @Test
    public void whenProductDoesNotExist_findById_throwsProductNotFoundException() {
        // arrange

        // act
        RuntimeException result = assertThrows(
                RuntimeException.class,
                () -> productService.findById(1)
        );

        // assert
        assertEquals(
                "The product with the id 1 was not found in the database",
                result.getMessage(),
                "Should be equal"
        );
    }

}