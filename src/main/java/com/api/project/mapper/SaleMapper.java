package com.api.project.mapper;

import com.api.project.dto.SaleRequest;
import com.api.project.model.Sale;
import org.springframework.stereotype.Component;

@Component
public class SaleMapper {
    public Sale saleRequestToOrder(SaleRequest saleRequest) {
        return new Sale(saleRequest.getPaymentMethod());
    }
}
