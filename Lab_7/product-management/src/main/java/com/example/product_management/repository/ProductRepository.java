package com.example.product_management.repository;

import com.example.product_management.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    // --- EXISTING METHODS ---
    List<Product> findByCategory(String category);
    
    // Task 7.2 & 7.3: Support sorting when filtering by category
    List<Product> findByCategory(String category, Sort sort);

    List<Product> findByNameContaining(String keyword);
    List<Product> findByPriceBetween(BigDecimal minPrice, BigDecimal maxPrice);
    List<Product> findByCategoryOrderByPriceAsc(String category);
    boolean existsByProductCode(String productCode);

    // --- TASK 5: ADVANCED SEARCH ---
    @Query("SELECT p FROM Product p WHERE " +
           "(:name IS NULL OR p.name LIKE %:name%) AND " +
           "(:category IS NULL OR p.category = :category) AND " +
           "(:minPrice IS NULL OR p.price >= :minPrice) AND " +
           "(:maxPrice IS NULL OR p.price <= :maxPrice)")
    List<Product> searchProducts(@Param("name") String name,
                                 @Param("category") String category,
                                 @Param("minPrice") BigDecimal minPrice,
                                 @Param("maxPrice") BigDecimal maxPrice);

    @Query("SELECT DISTINCT p.category FROM Product p ORDER BY p.category")
    List<String> findAllCategories();

    Page<Product> findByNameContaining(String keyword, Pageable pageable);

    // --- TASK 8.1: STATISTICS METHODS ---

    // Count products in a specific category
    @Query("SELECT COUNT(p) FROM Product p WHERE p.category = :category")
    long countByCategory(@Param("category") String category);

    // Calculate total value of inventory (Price * Quantity)
    @Query("SELECT SUM(p.price * p.quantity) FROM Product p")
    BigDecimal calculateTotalValue();

    // Calculate average product price
    @Query("SELECT AVG(p.price) FROM Product p")
    BigDecimal calculateAveragePrice();

    // Find products with low stock (below threshold)
    @Query("SELECT p FROM Product p WHERE p.quantity < :threshold")
    List<Product> findLowStockProducts(@Param("threshold") int threshold);

    // Get 5 most recent products (for dashboard activity feed)
    List<Product> findTop5ByOrderByCreatedAtDesc();
}