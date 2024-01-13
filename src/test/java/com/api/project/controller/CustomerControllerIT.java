package com.api.project.controller;

import com.api.project.mapper.CustomerMapper;
import com.api.project.model.Customer;
import com.api.project.service.CustomerService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = CustomerRestController.class)
class CustomerControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private final ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private CustomerService customerService;

    @MockBean
    private CustomerMapper customerMapper;

    @Test
    public void getCustomersWhoSubmitterReviewsToProductByProductId() throws Exception {
        // arrange
        int productId = 1;

        Customer customer1 = new Customer("Adrian Toma", "ado_tomescu@gmail.com");
        Customer customer2 = new Customer("Maria Anghel", "maria_anghel@gmail.com");

        List<Customer> expectedListOfCustomers = List.of(customer1, customer2);

        when(customerService.getCustomersWhoSubmitterReviewsToProductByProductId(productId))
                .thenReturn(expectedListOfCustomers);

        // act & assert
        mockMvc.perform(get("/customers/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(expectedListOfCustomers)))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(jsonPath("$[0].fullName").value("Adrian Toma"));
    }


    @Test
    @DisplayName("Get empty list of customers that submitted reviews for a product with no reviews - integration test")
    public void givenIdFrProductWithNoReviews_etCustomersWhoSubmitterReviewsToProductByProductId_returnEmptyList() throws Exception {
        // arrange
        // suppose we have a new product in the menu
        int productId = 10;

        when(customerService.getCustomersWhoSubmitterReviewsToProductByProductId(productId))
                .thenReturn(Collections.emptyList());

        // act & assert
        mockMvc.perform(get("/customers/products/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().is2xxSuccessful())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.length()").value(0));
    }
}