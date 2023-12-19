package com.api.project.controller;

import com.api.project.dto.CustomerRequest;
import com.api.project.dto.EmployeeRequest;
import com.api.project.mapper.CustomerMapper;
import com.api.project.model.Customer;
import com.api.project.model.Employee;
import com.api.project.service.CustomerService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
}
