package com.api.project.controller;

import com.api.project.dto.CustomerRequest;
import com.api.project.mapper.CustomerMapper;
import com.api.project.model.Customer;
import com.api.project.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/customers")
public class CustomerRestController {
    private final CustomerService customerService;
    private final CustomerMapper customerMapper;

    @Autowired
    public CustomerRestController(CustomerService customerService, CustomerMapper customerMapper) {
        this.customerService = customerService;
        this.customerMapper = customerMapper;
    }

    @PostMapping
    public ResponseEntity<Customer> create(@RequestBody @Valid CustomerRequest theCustomer) {
        return ResponseEntity
                    .ok()
                    .body(customerService.create(
                            customerMapper.customerRequestToCustomer(theCustomer)
                    ));
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<?> getCustomerById(@PathVariable int customerId) {
        return ResponseEntity.ok().body(customerService.findById(customerId));
    }

    @GetMapping
    public ResponseEntity<List<Customer>> findAll() {
        return ResponseEntity.ok().body(
                customerService.findAll()
        );
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<List<Customer>> getCustomersWhoSubmitterReviewsToProductByProductId(@PathVariable int productId) {
        return ResponseEntity.ok().body(customerService.getCustomersWhoSubmitterReviewsToProductByProductId(productId));
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<Customer> updateCustomer(@RequestBody @Valid CustomerRequest theCustomer,
                                                   @PathVariable int customerId) {
        return ResponseEntity.ok().body(
                customerService.update(customerMapper.customerRequestToCustomer(theCustomer), customerId)
        );
    }

    @DeleteMapping("/{customerId}")
    public String deleteCustomer(@PathVariable int customerId) {
        customerService.deleteById(customerId);
        return "The customer with the id: " + customerId + " was successfully deleted.";
    }

    @DeleteMapping
    public String deleteAllCustomers() {
        int numberOfCustomers = customerService.findAll().size();
        customerService.deleteAll();
        return "All " + numberOfCustomers + " customers were successfully deleted from the database.";
    }
}
