package com.example.product_management.service;

import com.example.product_management.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public interface ProductService {
    
    List<Product> getAllProducts();
    
    // Task 7.1: Get all products with sorting
    List<Product> getAllProducts(Sort sort);

    Optional<Product> getProductById(Long id);
    Product saveProduct(Product product);
    void deleteProduct(Long id);
    List<Product> searchProducts(String keyword);
    List<Product> getProductsByCategory(String category);

    // Task 7.3: Get products by category with sorting
    List<Product> getProductsByCategory(String category, Sort sort);

    List<Product> advancedSearch(String name, String category, BigDecimal minPrice, BigDecimal maxPrice);
    List<String> getAllCategories();
    Page<Product> searchProductsPaginated(String keyword, Pageable pageable);

    // --- TASK 8: DASHBOARD STATISTICS ---
    long getProductCount();
    long getCountByCategory(String category);
    BigDecimal getTotalInventoryValue();
    BigDecimal getAveragePrice();
    List<Product> getLowStockProducts(int threshold);
    List<Product> getRecentProducts();
}