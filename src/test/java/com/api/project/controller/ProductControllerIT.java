package com.api.project.controller;

import com.api.project.mapper.ProductMapper;
import com.api.project.model.Product;
import com.api.project.model.ProductType;
import com.api.project.model.Review;
import com.api.project.service.ProductService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import org.springframework.http.MediaType;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ProductRestController.class)
class ProductControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private ProductService productService;

    @MockBean
    private ProductMapper productMapper;

    @Test
    public void getProductByName() throws Exception {
        // arrange
        String productName = "Cappuccino";
        Product product = new Product();
        product.setProductName(productName);
        product.setProductType(ProductType.COFFEE);

        when(productService.getProductByName(productName)).thenReturn(product);

        // act & assert
        mockMvc.perform(get("/products/product-name/{productName}", productName))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.productName").value(product.getProductName()))
                .andExpect(jsonPath("$.productType").value(ProductType.COFFEE.toString()));
    }

    @Test
    public void getAveragePriceOfProductType() throws Exception {
        // arrange
        ProductType productType = ProductType.COFFEE;
        double expectedAveragePrice = 10.0;

        // act & assert
        when(productService.getAveragePriceForProductType(productType)).thenReturn(expectedAveragePrice);

        mockMvc.perform(get("/products/average-price/{productType}", productType)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(String.valueOf(expectedAveragePrice)));
    }

    @Test
    public void findProductAndReviewsByProductId() throws Exception {
        // arrange
        int productId = 20;
        Product product = new Product();
        product.setProductId(productId);

        Review review1 = new Review(); review1.setReviewId(1);
        Review review2 = new Review(); review2.setReviewId(2);

        when(productService.findProductAndReviewsByProductId(productId)).thenReturn(List.of(review1, review2));

        // act & assert
        mockMvc.perform(get("/products/{productId}/reviews", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].reviewId").value(review1.getReviewId()))
                .andExpect(jsonPath("$[1].reviewId").value(review2.getReviewId()));
    }

    @Test
    public void getProductOrderedBetweenDates() throws Exception {
        // arrange
        LocalDate date1 = LocalDate.of(2024, 1, 1);
        LocalDate date2 = LocalDate.of(2024, 1, 10);

        Product product1 = new Product();
        Product product2 = new Product();
        Product product3 = new Product();

        List<Product> expectedListOfProducts = List.of(product1, product2, product3);

        when(productService.getProductsOrderedBetweenDates(date1, date2)).thenReturn(expectedListOfProducts);

        // act & assert
        mockMvc.perform(get("/products/sales/sold")
                        .param("dateStart", date1.toString())
                        .param("dateEnd", date2.toString())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedListOfProducts)));
    }

    @Test
    public void getProductsWithoutAnyReview() throws Exception {
        // arrange
        List<Product> expectedProducts = new ArrayList<>();

        when(productService.getProductsWithoutAnyReview()).thenReturn(expectedProducts);

        // act & assert
        mockMvc.perform(get("/products/no-reviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedProducts)));
    }

    @Test
    public void getAverageRatingAndReviewCountPerProduct() throws Exception {
        // arrange
        List<Object[]> expectedResults = new ArrayList<>();

        when(productService.getAverageRatingAndReviewCountPerProduct()).thenReturn(expectedResults);

        // act & assert
        mockMvc.perform(get("/products/average-rating/number-of-reviews")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedResults)));
    }

    @Test
    public void addDiscountToProduct() throws Exception {
        // arrange
        Double discount = 20.0;
        int productId = 1;

        Product product = new Product();
        product.setPrice(100);

        // Act & Assert
        mockMvc.perform(put("/products/discount/{productId}", productId)
                        .param("discount", String.valueOf(discount))
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful());

        verify(productService).updatePriceDuringChristmasHoliday(discount, productId);
    }

}