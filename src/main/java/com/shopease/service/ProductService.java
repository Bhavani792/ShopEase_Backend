package com.shopease.service;

import java.math.BigDecimal;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.shopease.dto.ProductRequest;
import com.shopease.entity.Product;
import com.shopease.repository.ProductRepository;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product addProduct(ProductRequest request) {

        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stock(request.getStock())
                .category(request.getCategory())
                .imageUrl(request.getImageUrl())
                .build();

        return productRepository.save(product);
    }

    public Page<Product> getProducts(
            String search,
            String category,
            BigDecimal minPrice,
            BigDecimal maxPrice,
            String sort,
            Pageable pageable) {

        Specification<Product> specification =
                Specification.unrestricted();

        // Search by product name
        if (search != null && !search.trim().isEmpty()) {

            String searchText = search.trim().toLowerCase();

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.like(
                                    criteriaBuilder.lower(
                                            root.get("name")
                                    ),
                                    "%" + searchText + "%"
                            )
            );
        }

        // Filter by category
        if (category != null && !category.trim().isEmpty()) {

            String categoryText =
                    category.trim().toLowerCase();

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.equal(
                                    criteriaBuilder.lower(
                                            root.get("category")
                                    ),
                                    categoryText
                            )
            );
        }

        // Minimum price
        if (minPrice != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.greaterThanOrEqualTo(
                                    root.get("price"),
                                    minPrice
                            )
            );
        }

        // Maximum price
        if (maxPrice != null) {

            specification = specification.and(
                    (root, query, criteriaBuilder) ->
                            criteriaBuilder.lessThanOrEqualTo(
                                    root.get("price"),
                                    maxPrice
                            )
            );
        }

        return productRepository.findAll(
                specification,
                pageable
        );
    }

    public Product getProductById(Long id) {

        return productRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Product not found"));
    }

    public Product updateProduct(
            Long id,
            ProductRequest request) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"));

        product.setName(request.getName());
        product.setDescription(request.getDescription());
        product.setPrice(request.getPrice());
        product.setStock(request.getStock());
        product.setCategory(request.getCategory());
        product.setImageUrl(request.getImageUrl());

        return productRepository.save(product);
    }

    public String deleteProduct(Long id) {

        Product product =
                productRepository.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Product not found"));

        productRepository.delete(product);

        return "Product deleted successfully";
    }
}