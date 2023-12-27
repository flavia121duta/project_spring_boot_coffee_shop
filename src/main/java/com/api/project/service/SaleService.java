package com.api.project.service;

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
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    @Transactional
    public Sale createSale(SaleRequest saleRequest) {
        Optional<Employee> theEmployee = employeeRepository.findById(saleRequest.getEmployeeId());
        if(theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException(saleRequest.getEmployeeId());
        }

        List<Product> theProducts = new ArrayList<>();
        for (Integer productId: saleRequest.getProductIds()) {
            Product product = productRepository.findById(productId).orElseThrow(
                    () -> new ProductNotFoundException(productId));
            theProducts.add(product);
        }

        Sale theSale = new Sale();
        theSale.setPaymentMethod(saleRequest.getPaymentMethod());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        theSale.setDateOfOrder(LocalDateTime.now().format(formatter));

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
            throw new SaleNotFoundException(theId);
        }
        return theSale;
    }

    public List<Integer> getSalesThatContainProductByProductId(int productId) {
        Optional<Product> theProduct = productRepository.findById(productId);
        if(theProduct.isEmpty()) {
            throw new ProductNotFoundException(productId);
        }

        List<Sale> allSales = saleRepository.findAll();
        List<Sale> salesThatContainsTheProduct = new ArrayList<>();
        for(Sale theSale: allSales) {
            if (theSale.getProducts().contains(theProduct.get())) {
                salesThatContainsTheProduct.add(theSale);
            }
        }

        List<Integer> ids = new ArrayList<>();
        for(Sale sale: salesThatContainsTheProduct) {
            int theId = sale.getSaleId();
            ids.add(theId);
        }
        return ids;
    }

    public double getTotalPriceOfSaleBySaleId(int saleId) {
        Optional<Sale> theSale = saleRepository.findById(saleId);
        if(theSale.isEmpty()) {
            throw new SaleNotFoundException(saleId);
        }

        return theSale.get().getTotalPrice();
    }

    public List<Integer> findSalesTakenByEmployeeGivenByEmployeeId(int employeeId) {
        Optional<Employee> theEmployee = employeeRepository.findById(employeeId);
        if (theEmployee.isEmpty()) {
            throw new EmployeeNotFoundException(employeeId);
        }

        return saleRepository.findSalesTakenByEmployeeGivenByEmployeeId(employeeId);
    }

    public Sale update(Sale newSale, int saleId) {
        Optional<Sale> theSale = saleRepository.findById(saleId);
        if (theSale.isEmpty()) {
            throw new SaleNotFoundException(saleId);
        }

        Sale dbSale = theSale.get();
        dbSale.setProducts(newSale.getProducts());
        dbSale.setEmployee(newSale.getEmployee());
        dbSale.setDateOfOrder(newSale.getDateOfOrder());
        dbSale.setPaymentMethod(newSale.getPaymentMethod());

        return saleRepository.save(dbSale);
    }

    public void deleteById(int saleId) {
        Optional<Sale> theSale = saleRepository.findById(saleId);
        if(theSale.isEmpty()) {
            throw new SaleNotFoundException(saleId);
        }

        saleRepository.deleteById(saleId);
    }

    public void deleteAll() {
        saleRepository.deleteAll();
    }
}
