package com.api.project.model;

import jakarta.persistence.*;
import org.springframework.data.relational.core.sql.In;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Sale {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int saleId;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private LocalDate dateOfOrder;

    @ManyToOne
    @JoinColumn(name = "employee_id")
    private Employee employee;

    @ManyToMany
    @JoinTable(name = "sale_product",
            joinColumns = @JoinColumn(name = "sale_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> products = new ArrayList<>();


    public Sale() {}

    public Sale(PaymentMethod paymentMethod, LocalDate dateOfOrder) {
        this.paymentMethod = paymentMethod;
        this.dateOfOrder = dateOfOrder;
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

    public LocalDate getDateOfOrder() {
        return dateOfOrder;
    }

    public void setDateOfOrder(LocalDate dateOfOrder) {
        this.dateOfOrder = dateOfOrder;
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
