package com.shopease.controller;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.shopease.dto.ProductRequest;
import com.shopease.entity.Product;
import com.shopease.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    // ==========================================
    // GET PRODUCTS
    // USER + ADMIN
    // ==========================================
    @GetMapping
    public ResponseEntity<Page<Product>> getProducts(

            @RequestParam(required = false)
            String search,

            @RequestParam(required = false)
            String category,

            @RequestParam(required = false)
            BigDecimal minPrice,

            @RequestParam(required = false)
            BigDecimal maxPrice,

            @RequestParam(required = false)
            String sort,

            @RequestParam(defaultValue = "0")
            int page,

            @RequestParam(defaultValue = "10")
            int size) {

        Sort sorting = Sort.by(
                Sort.Direction.ASC,
                "id"
        );

        if ("priceAsc".equalsIgnoreCase(sort)) {

            sorting = Sort.by(
                    Sort.Direction.ASC,
                    "price"
            );

        } else if ("priceDesc".equalsIgnoreCase(sort)) {

            sorting = Sort.by(
                    Sort.Direction.DESC,
                    "price"
            );
        }

        Pageable pageable = PageRequest.of(
                page,
                size,
                sorting
        );

        return ResponseEntity.ok(
                productService.getProducts(
                        search,
                        category,
                        minPrice,
                        maxPrice,
                        sort,
                        pageable
                )
        );
    }

    // ==========================================
    // GET PRODUCT BY ID
    // USER + ADMIN
    // ==========================================
    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productService.getProductById(id)
        );
    }

    // ==========================================
    // ADD PRODUCT
    // ADMIN ONLY
    // ==========================================
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping
    public ResponseEntity<Product> addProduct(
            @Valid @RequestBody ProductRequest request) {

        return new ResponseEntity<>(
                productService.addProduct(request),
                HttpStatus.CREATED
        );
    }

    // ==========================================
    // UPDATE PRODUCT
    // ADMIN ONLY
    // ==========================================
    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest request) {

        return ResponseEntity.ok(
                productService.updateProduct(
                        id,
                        request
                )
        );
    }

    // ==========================================
    // DELETE PRODUCT
    // ADMIN ONLY
    // ==========================================
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProduct(
            @PathVariable Long id) {

        return ResponseEntity.ok(
                productService.deleteProduct(id)
        );
    }
}