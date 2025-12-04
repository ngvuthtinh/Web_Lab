package com.example.product_management.controller;

import com.example.product_management.entity.Product;
import com.example.product_management.service.ProductService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.util.StringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Controller
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // Task 7: Combined Sorting and Filtering
    @GetMapping
    public String listProducts(
            @RequestParam(required = false) String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            @RequestParam(required = false) String category,
            Model model) {

        // Default Sort
        Sort sort = Sort.by("id").ascending();
        if (sortBy != null && !sortBy.isEmpty()) {
            sort = sortDir.equalsIgnoreCase("asc") ?
                    Sort.by(sortBy).ascending() :
                    Sort.by(sortBy).descending();
        }

        // Handle "All Categories" logic
        if (category != null && category.trim().isEmpty()) {
            category = null;
        }

        List<Product> products;
        if (category != null) {
            products = productService.getProductsByCategory(category, sort);
        } else {
            products = productService.getAllProducts(sort);
        }

        model.addAttribute("products", products);
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("currentCategory", category);
        model.addAttribute("categories", productService.getAllCategories());

        return "product-list";
    }

    @GetMapping("/new")
    public String showNewForm(Model model) {
        Product product = new Product();
        model.addAttribute("product", product);
        return "product-form";
    }

    @GetMapping("/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model, RedirectAttributes redirectAttributes) {
        return productService.getProductById(id)
                .map(product -> {
                    model.addAttribute("product", product);
                    return "product-form";
                })
                .orElseGet(() -> {
                    redirectAttributes.addFlashAttribute("error", "Product not found");
                    return "redirect:/products";
                });
    }

    // --- MODIFIED SAVE METHOD FOR BONUS 2 (IMAGE UPLOAD) ---
    // Task 6: Validation added
    @PostMapping("/save")
    public String saveProduct(
            @Valid @ModelAttribute("product") Product product,
            BindingResult result,
            @RequestParam("image") MultipartFile multipartFile, // Bonus 2: Accept file
            Model model,
            RedirectAttributes redirectAttributes) {
        
        if (result.hasErrors()) {
            return "product-form";
        }

        try {
            // BONUS 2: Logic to save image
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            
            if (!fileName.isEmpty()) {
                // User uploaded a new image
                product.setImagePath(fileName);
                Product savedProduct = productService.saveProduct(product);
                
                String uploadDir = "uploads/";
                Path uploadPath = Paths.get(uploadDir);
                
                if (!Files.exists(uploadPath)) {
                    Files.createDirectories(uploadPath);
                }
                
                try (InputStream inputStream = multipartFile.getInputStream()) {
                    Path filePath = uploadPath.resolve(fileName);
                    Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
                }
            } else {
                // No new image, try to keep old one if editing
                if (product.getId() != null) {
                    Product existing = productService.getProductById(product.getId()).orElse(null);
                    if (existing != null) {
                        product.setImagePath(existing.getImagePath());
                    }
                }
                productService.saveProduct(product);
            }

            redirectAttributes.addFlashAttribute("message",
                    product.getId() == null ? "Product added successfully!" : "Product updated successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error saving product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        try {
            productService.deleteProduct(id);
            redirectAttributes.addFlashAttribute("message", "Product deleted successfully!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("error", "Error deleting product: " + e.getMessage());
        }
        return "redirect:/products";
    }

    // Task 5.3: Search with Pagination
    @GetMapping("/search")
    public String searchProducts(
            @RequestParam("keyword") String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, size);
        Page<Product> productPage = productService.searchProductsPaginated(keyword, pageable);

        model.addAttribute("products", productPage.getContent());
        model.addAttribute("keyword", keyword);
        model.addAttribute("currentPage", page);
        model.addAttribute("totalPages", productPage.getTotalPages());
        model.addAttribute("categories", productService.getAllCategories());
        
        return "product-list";
    }

    // Task 5.1: Advanced Search
    @GetMapping("/advanced-search")
    public String advancedSearch(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) BigDecimal minPrice,
            @RequestParam(required = false) BigDecimal maxPrice,
            Model model) {
        
        if (category != null && category.trim().isEmpty()) category = null;
        if (name != null && name.trim().isEmpty()) name = null;

        List<Product> products = productService.advancedSearch(name, category, minPrice, maxPrice);
        model.addAttribute("products", products);
        model.addAttribute("categories", productService.getAllCategories());
        
        return "product-list";
    }
}