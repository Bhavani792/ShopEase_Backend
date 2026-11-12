package com.shopease.service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import com.shopease.entity.Product;
import com.shopease.repository.ProductRepository;

@Service
public class GeminiService {

    @Value("${gemini.api.key}")
    private String apiKey;

    @Value("${gemini.api.url}")
    private String apiUrl;

    private final RestClient restClient;
    private final ProductRepository productRepository;

    public GeminiService(ProductRepository productRepository) {
        this.restClient = RestClient.builder().build();
        this.productRepository = productRepository;
    }

    public String getShoppingAdvice(String question) {

        // Fetch products from MySQL
        List<Product> products = productRepository.findAll()
                .stream()
                .filter(product -> product.getStock() != null
                        && product.getStock() > 0)
                .toList();

        if (products.isEmpty()) {
            return "Sorry, no products are currently available.";
        }

        // Prepare actual product details
        StringBuilder productDetails = new StringBuilder();

        for (Product product : products) {
            productDetails.append("\nProduct Name: ")
                    .append(product.getName())
                    .append("\nCategory: ")
                    .append(product.getCategory())
                    .append("\nPrice: ₹")
                    .append(product.getPrice())
                    .append("\nDescription: ")
                    .append(product.getDescription())
                    .append("\nAvailable Stock: ")
                    .append(product.getStock())
                    .append("\n");
        }

        // Create AI prompt
        String prompt = """
                You are ShopEase's AI Shopping Assistant.

                Recommend products ONLY from the product list below.
                Never invent products, prices, or stock information.

                Understand the customer's needs and budget.
                Recommend the most suitable available products.
                Explain briefly why each product is suitable.

                If no product matches the customer's requirements,
                clearly say that no suitable product is available.

                AVAILABLE SHOP EASE PRODUCTS:
                """ + productDetails
                + "\nCUSTOMER QUESTION: " + question;

        Map<String, Object> requestBody = Map.of(
                "contents", List.of(
                        Map.of(
                                "parts", List.of(
                                        Map.of("text", prompt)
                                )
                        )
                )
        );

        try {

            Map<?, ?> response = restClient.post()
                    .uri(apiUrl)
                    .header("x-goog-api-key", apiKey)
                    .contentType(MediaType.APPLICATION_JSON)
                    .body(requestBody)
                    .retrieve()
                    .body(Map.class);

            if (response == null) {
                return "Sorry, no response received from AI.";
            }

            List<?> candidates =
                    (List<?>) response.get("candidates");

            if (candidates == null || candidates.isEmpty()) {
                return "Sorry, no recommendations are available.";
            }

            Map<?, ?> candidate = (Map<?, ?>) candidates.get(0);

            Map<?, ?> content = (Map<?, ?>) candidate.get("content");

            List<?> parts = (List<?>) content.get("parts");

            Map<?, ?> firstPart = (Map<?, ?>) parts.get(0);

            return String.valueOf(firstPart.get("text"));

        } catch (Exception e) {

            throw new RuntimeException(
                    "Unable to get AI response: " + e.getMessage());
        }
    }
}