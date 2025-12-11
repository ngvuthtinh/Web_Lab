package com.example.customer_api.controller;

import com.example.customer_api.dto.CustomerResponseDTO;
import com.example.customer_api.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v2/customers")
@CrossOrigin(origins = "*")
public class CustomerRestControllerV2 {

    private final CustomerService customerService;

    @Autowired
    public CustomerRestControllerV2(CustomerService customerService) {
        this.customerService = customerService;
    }

    // Enhanced GET: Wraps data in "data" object and pagination in "meta" object
    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCustomersV2(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Page<CustomerResponseDTO> customerPage = customerService.getAllCustomers(page, size, sortBy, sortDir);

        Map<String, Object> response = new HashMap<>();
        
        response.put("data", customerPage.getContent());
        
        Map<String, Object> meta = new HashMap<>();
        meta.put("page", customerPage.getNumber());
        meta.put("size", customerPage.getSize());
        meta.put("totalElements", customerPage.getTotalElements());
        meta.put("totalPages", customerPage.getTotalPages());
        
        response.put("meta", meta);
        response.put("apiVersion", "v2.0"); 

        return ResponseEntity.ok(response);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getCustomerByIdV2(@PathVariable Long id) {
        CustomerResponseDTO customer = customerService.getCustomerById(id);
        
        Map<String, Object> response = new HashMap<>();
        response.put("data", customer);
        response.put("apiVersion", "v2.0");
        
        return ResponseEntity.ok(response);
    }
}