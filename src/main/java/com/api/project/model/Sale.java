package com.api.project.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int saleId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String timeOfOrder;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToMany
    @JoinTable(name = "sale_product",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();


    public Sale() {}

    public Sale(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public int getSaleId() {
        return saleId;
    }

    public void setSaleId(int saleId) {
        this.saleId = saleId;
    }

    public PaymentMethod getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(PaymentMethod paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getDateOfOrder() {
        return timeOfOrder;
    }

    public void setDateOfOrder(String timeOfOrder) {
        this.timeOfOrder = timeOfOrder;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public List<Product> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        double sum = 0;
        for(Product product: products) {
            sum += product.getPrice();
        }

        return sum;
    }

    public void addProduct(Product theProduct) {
        products.add(theProduct);
    }
}
