package com.api.project.service;

import ch.qos.logback.core.joran.conditional.ThenAction;
import com.api.project.dto.SaleRequest;
import com.api.project.exception.EmployeeNotFoundException;
import com.api.project.exception.ProductNotFoundException;
import com.api.project.exception.SaleNotFoundException;
import com.api.project.model.Employee;
import com.api.project.model.Product;
import com.api.project.model.Sale;
import com.api.project.repository.EmployeeRepository;
import com.api.project.repository.ProductRepository;
import com.api.project.repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class SaleService {
    private final SaleRepository saleRepository;

    private final EmployeeRepository employeeRepository;

    private final ProductRepository productRepository;

    @Autowired
    public SaleService(SaleRepository saleRepository, EmployeeRepository employeeRepository, ProductRepository productRepository) {
        this.saleRepository = saleRepository;
        this.employeeRepository = employeeRepository;
        this.productRepository = productRepository;
    }

    public Sale createSale(SaleRequest saleRequest) {
        Optional<Employee> theEmployee = employeeRepository.findById(saleRequest.getEmployeeId());
        if(theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException("The employee with the id " + saleRequest.getEmployeeId() + " does not exist");
        }

        List<Product> theProducts = new ArrayList<>();
        for (Integer productId: saleRequest.getProductIds()) {
            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new ProductNotFoundException("The product with the id " + productId + " was not found"));
            theProducts.add(product);
        }

        Sale theSale = new Sale();
        theSale.setPaymentMethod(saleRequest.getPaymentMethod());
        theSale.setDateOfOrder(saleRequest.getDateOfOrder());
        theSale.setEmployee(theEmployee.get());
        theSale.setProducts(theProducts);

        return saleRepository.save(theSale);
    }

    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    public Optional<Sale> findById(int theId) {
        Optional<Sale> theSale = saleRepository.findById(theId);
        if(theSale.isEmpty()) {
            throw new SaleNotFoundException("The sale with the id " + theId + " was not found");
        }
        return theSale;
    }

    public List<Sale> getSaleThatContainsProduct(int productId) {
        Optional<Product> theProduct = productRepository.findById(productId);
        if(theProduct.isEmpty()) {
            throw new ProductNotFoundException("The product with the id " + productId + " was not found");
        }

        List<Sale> allSales = saleRepository.findAll();
        List<Sale> salesThatContainsTheProduct = new ArrayList<>();
        for(Sale theSale: allSales) {
            if (theSale.getProducts().contains(theProduct.get())) {
                salesThatContainsTheProduct.add(theSale);
            }
        }
        return salesThatContainsTheProduct;
    }

    public double getTotalPriceOfSaleBySaleId(int saleId) {
        Optional<Sale> theSale = saleRepository.findById(saleId);
        if(theSale.isEmpty()) {
            throw new SaleNotFoundException("The sale with the id " + saleId + " was not found");
        }

        return theSale.get().getTotalPrice();
    }


}
