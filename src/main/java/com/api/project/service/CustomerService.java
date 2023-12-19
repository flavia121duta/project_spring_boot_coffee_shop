package com.api.project.service;

import com.api.project.exception.CustomerNotFoundException;
import com.api.project.model.Customer;
import com.api.project.repository.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CustomerService {
    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public Customer create(Customer theCustomer) {
        return customerRepository.save(theCustomer);
    }

    public List<Customer> findAll() {
        return customerRepository.findAll();
    }

    public Optional<Customer> findById(int theId) {
        Optional<Customer> theCustomer = customerRepository.findById(theId);

        if (theCustomer.isEmpty()) {
            throw new CustomerNotFoundException("The customer with the id: " + theId + " was not found");
        }

        return theCustomer;
    }

    public Customer update(Customer newCustomer, int theId) {
        Optional<Customer> theCustomer = customerRepository.findById(theId);

        if (theCustomer.isEmpty()) {
            throw new CustomerNotFoundException("The customer with the id: " + theId + " was not found");
        }

        Customer dbCustomer = theCustomer.get();
        dbCustomer.setEmail(newCustomer.getEmail());
        dbCustomer.setFullName(newCustomer.getFullName());
        dbCustomer.setReviews(newCustomer.getReviews());

        return customerRepository.save(dbCustomer);
    }

    public void deleteById(int theId) {
        Optional<Customer> theCustomer = customerRepository.findById(theId);

        if(theCustomer.isEmpty()) {
            throw new CustomerNotFoundException("The customer with the id: " + theId + " was not found");
        }

        customerRepository.deleteById(theId);
    }

    public void deleteAll() {
        customerRepository.deleteAll();
    }
}
