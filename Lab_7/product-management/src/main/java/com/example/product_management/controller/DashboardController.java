package com.example.product_management.controller;

import com.example.product_management.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

// Task 8.2: Create Dashboard Controller
@Controller
@RequestMapping("/dashboard")
public class DashboardController {

    private final ProductService productService;

    @Autowired
    public DashboardController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String showDashboard(Model model) {
        // Task 8.3: Add statistics to model
        
        // 1. General Stats (Total Count, Value, Average Price)
        model.addAttribute("totalProducts", productService.getProductCount());
        model.addAttribute("totalValue", productService.getTotalInventoryValue());
        model.addAttribute("averagePrice", productService.getAveragePrice());

        // 2. Category Distribution (Map<CategoryName, Count>)
        List<String> categories = productService.getAllCategories();
        Map<String, Long> categoryStats = new HashMap<>();
        for (String cat : categories) {
            categoryStats.put(cat, productService.getCountByCategory(cat));
        }
        model.addAttribute("categoryStats", categoryStats);

        // 3. Low Stock Alerts (Define threshold as 10)
        model.addAttribute("lowStockProducts", productService.getLowStockProducts(10));

        // 4. Recent Activity (Last 5 added)
        model.addAttribute("recentProducts", productService.getRecentProducts());

        return "dashboard";
    }
}