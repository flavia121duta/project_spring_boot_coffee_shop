package com.api.project.dto;

import com.api.project.model.ProductType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ProductRequest {
    @NotNull(message = "The name of the product should not be null!")
    @NotBlank(message = "The name of the product should not be blank!")
    private String productName;

    @NotNull(message = "The price should not be null!")
    @Min(1)
    @Max(100)
    private double price;

    private ProductType productType;

    public ProductRequest() {
    }

    public ProductRequest(String productName, double price, ProductType productType) {
        this.productName = productName;
        this.price = price;
        this.productType = productType;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public ProductType getProductType() {
        return productType;
    }

    public void setProductType(ProductType productType) {
        this.productType = productType;
    }
}
