package com.api.project.dto;

import com.api.project.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class SaleRequest {
    @NotNull
    private PaymentMethod paymentMethod;

    private LocalDateTime timeOfOrder;

    @NotNull
    private int employeeId;

    @NotNull
    private List<Integer> productIds;

    public SaleRequest() {
    }

    public SaleRequest(PaymentMethod paymentMethod, LocalDate dateOfOrder) {
        this.paymentMethod = paymentMethod;
    }

    public SaleRequest(PaymentMethod paymentMethod, int employeeId, List<Integer> productIds) {
        this.paymentMethod = paymentMethod;
        this.employeeId = employeeId;
        this.productIds = productIds;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDateTime getDateOfOrder() {
        return timeOfOrder;
    }

    public void setDateOfOrder(LocalDateTime dateOfOrder) {
        this.timeOfOrder = dateOfOrder;
    }

    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public List<Integer> getProductIds() {
        return productIds;
    }

    public void setProductIds(List<Integer> productIds) {
        this.productIds = productIds;
    }
}
