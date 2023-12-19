package com.api.project.mapper;

import com.api.project.dto.ProductRequest;
import com.api.project.model.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {
    public Product productRequestToProduct(ProductRequest productRequest) {
        return new Product(productRequest.getProductName(), productRequest.getPrice(), productRequest.getProductType());
    }
}
