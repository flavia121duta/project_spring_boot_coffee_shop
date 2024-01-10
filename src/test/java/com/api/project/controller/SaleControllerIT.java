package com.api.project.controller;

import com.api.project.mapper.SaleMapper;
import com.api.project.service.SaleService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = SaleRestController.class)
class SaleControllerIT {
    @Autowired
    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @MockBean
    private SaleService saleService;

    @MockBean
    private SaleMapper saleMapper;

    @Test
    public void getTotalPrice() throws Exception {
        // arrange
        int saleId = 1;
        double expectedTotalPrice = 56.5;

        when(saleService.getTotalPriceOfSaleBySaleId(saleId)).thenReturn(expectedTotalPrice);

        // act & assert
        mockMvc.perform(get("/sales/{saleId}/total", saleId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().string(String.valueOf(expectedTotalPrice)));
    }

    @Test
    @DisplayName("Get the sales that contain certain product by product id - integration test")
    public void getAllSalesByProductId() throws Exception {
        // arrange
        int productId = 1;
        List<Integer> expectedSaleIds = Arrays.asList(1, 2, 3);

        when(saleService.getSalesThatContainProductByProductId(productId)).thenReturn(expectedSaleIds);

        // act & assert
        mockMvc.perform(get("/sales/product/{productId}", productId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedSaleIds)));
    }

    @Test
    public void findSalesTakenByEmployeeGivenByEmployeeId() throws Exception {
        // arrange
        int employeeId = 1;
        List<Integer> expectedSaleIds = Arrays.asList(1, 2, 3);

        when(saleService.findSalesTakenByEmployeeGivenByEmployeeId(employeeId)).thenReturn(expectedSaleIds);

        // act & assert
        mockMvc.perform(get("/sales/employee/{employeeId}", employeeId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().is2xxSuccessful())
                .andExpect(content().json(objectMapper.writeValueAsString(expectedSaleIds)));
    }

}