package com.example.customer_api.service;

import com.example.customer_api.dto.CustomerRequestDTO;
import com.example.customer_api.dto.CustomerResponseDTO;
import com.example.customer_api.dto.CustomerUpdateDTO; // Import mới
import org.springframework.data.domain.Page;

import java.util.List;

public interface CustomerService {
    
    // Task 6.1, 6.2, 6.3: Pagination & Sorting
    Page<CustomerResponseDTO> getAllCustomers(int page, int size, String sortBy, String sortDir);
    
    // Old method kept for compatibility
    List<CustomerResponseDTO> getAllCustomers();
    
    CustomerResponseDTO getCustomerById(Long id);
    
    CustomerResponseDTO createCustomer(CustomerRequestDTO requestDTO);
    
    CustomerResponseDTO updateCustomer(Long id, CustomerRequestDTO requestDTO);
    
    // Task 7.2 Implementation: Partial Update
    CustomerResponseDTO partialUpdateCustomer(Long id, CustomerUpdateDTO updateDTO);
    
    void deleteCustomer(Long id);
    
    // Task 5.1: Search
    List<CustomerResponseDTO> searchCustomers(String keyword);
    
    // Task 5.2: Filter
    List<CustomerResponseDTO> getCustomersByStatus(String status);

    // Task 5.3: Advanced Search
    List<CustomerResponseDTO> advancedSearch(String name, String email, String status);
}