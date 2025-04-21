package com.bitsvalley.babanaa.services.Agent;

import com.bitsvalley.babanaa.domains.Agent.Customer;
import com.bitsvalley.babanaa.repositories.Agent.CustomerRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomerService {
    @Autowired private CustomerRepository customerRepository;

    public List<Customer> getAllCustomers() {
        return customerRepository.findAll();
    }

    public Customer getCustomerById(Long id) {
        return customerRepository.findById(id).orElse(null);
    }

    public Customer saveCustomer(Customer customer) {
        return customerRepository.save(customer);
    }

    public Customer updateCustomer(Long id, Customer customer) {
        Customer existingCustomer = customerRepository.findById(id).orElse(null);
        if (existingCustomer != null) {
            existingCustomer.setAddress(customer.getAddress());
            existingCustomer.setContactInfo(customer.getContactInfo());
            existingCustomer.setCollections(customer.getCollections());
            existingCustomer.setLocation(customer.getLocation());
            return customerRepository.save(existingCustomer);
        }
        return null;
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

//    public List<Customer> getAllCollectionsForCustomer(Long id) {
//        return customerRepository.getAllCollectionsForCustomer(id);
//    }
}
