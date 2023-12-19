package com.api.project.mapper;

import com.api.project.dto.CustomerRequest;
import com.api.project.model.Customer;
import org.springframework.stereotype.Component;

@Component
public class CustomerMapper {
    public Customer customerRequestToCustomer(CustomerRequest customerRequest) {
        return new Customer(customerRequest.getFullName(), customerRequest.getEmail());
    }
}
