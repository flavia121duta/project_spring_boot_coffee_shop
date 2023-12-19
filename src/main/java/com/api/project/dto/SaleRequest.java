package com.api.project.dto;

import com.api.project.model.PaymentMethod;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.List;

public class SaleRequest {
    @NotNull
    private PaymentMethod paymentMethod;

    private LocalDate dateOfOrder;

    private int employeeId;

    private List<Integer> productIds;

    public SaleRequest() {
    }

    public SaleRequest(PaymentMethod paymentMethod, LocalDate dateOfOrder) {
        this.paymentMethod = paymentMethod;
        this.dateOfOrder = dateOfOrder;
    }

    public SaleRequest(PaymentMethod paymentMethod, LocalDate dateOfOrder, int employeeId, List<Integer> productIds) {
        this.paymentMethod = paymentMethod;
        this.dateOfOrder = dateOfOrder;
        this.employeeId = employeeId;
        this.productIds = productIds;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public LocalDate getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDate dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
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
