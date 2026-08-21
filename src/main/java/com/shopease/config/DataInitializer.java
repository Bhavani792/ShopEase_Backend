package com.shopease.config;

import com.shopease.entity.Product;
import com.shopease.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigDecimal;
import java.util.List;

@Configuration
@RequiredArgsConstructor
public class DataInitializer {

    private final ProductRepository productRepository;

    @Bean
    CommandLineRunner loadProducts() {
        return args -> {

            // Don't duplicate products every time the backend starts
            if (productRepository.count() >= 20) {
                return;
            }

            List<Product> products = List.of(

                // =========================
                // ELECTRONICS
                // =========================

                Product.builder()
                        .name("Wireless Headphones")
                        .description("Bluetooth wireless headphones with clear sound quality, comfortable ear cushions and long battery life.")
                        .price(new BigDecimal("2499"))
                        .stock(25)
                        .category("Electronics")
                        .imageUrl("https://images.unsplash.com/photo-1505740420928-5e560c06d30e?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Gaming Mouse")
                        .description("Wireless gaming mouse with accurate tracking, ergonomic design and programmable buttons.")
                        .price(new BigDecimal("1999"))
                        .stock(49)
                        .category("Electronics")
                        .imageUrl("https://images.unsplash.com/photo-1527814050087-3793815479db?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Mechanical Gaming Keyboard")
                        .description("RGB mechanical gaming keyboard with responsive switches and a durable compact design.")
                        .price(new BigDecimal("3499"))
                        .stock(32)
                        .category("Electronics")
                        .imageUrl("https://images.unsplash.com/photo-1587829741301-dc798b83add3?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Smart Watch")
                        .description("Modern smart watch with fitness tracking, notifications, heart-rate monitoring and multiple sports modes.")
                        .price(new BigDecimal("2999"))
                        .stock(18)
                        .category("Electronics")
                        .imageUrl("https://images.unsplash.com/photo-1523275335684-37898b6baf30?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Bluetooth Speaker")
                        .description("Portable Bluetooth speaker with powerful audio, compact design and long battery backup.")
                        .price(new BigDecimal("1799"))
                        .stock(41)
                        .category("Electronics")
                        .imageUrl("https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("USB-C Fast Charger")
                        .description("Fast USB-C wall charger suitable for smartphones, tablets and other compatible devices.")
                        .price(new BigDecimal("899"))
                        .stock(75)
                        .category("Electronics")
                        .imageUrl("https://images.unsplash.com/photo-1583863788434-e58a36330cf0?auto=format&fit=crop&w=800&q=80")
                        .build(),

                // =========================
                // FASHION
                // =========================

                Product.builder()
                        .name("Men's Casual T-Shirt")
                        .description("Comfortable cotton casual t-shirt suitable for everyday wear.")
                        .price(new BigDecimal("699"))
                        .stock(60)
                        .category("Fashion")
                        .imageUrl("https://images.unsplash.com/photo-1521572163474-6864f9cf17ab?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Men's Denim Jacket")
                        .description("Classic denim jacket with a modern fit, suitable for casual outings.")
                        .price(new BigDecimal("1899"))
                        .stock(24)
                        .category("Fashion")
                        .imageUrl("https://images.unsplash.com/photo-1551028719-00167b16eac5?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Women's Handbag")
                        .description("Stylish everyday handbag with spacious compartments and a modern design.")
                        .price(new BigDecimal("1599"))
                        .stock(28)
                        .category("Fashion")
                        .imageUrl("https://images.unsplash.com/photo-1584917865442-de89df76afd3?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Running Shoes")
                        .description("Lightweight running shoes with comfortable cushioning for walking and workouts.")
                        .price(new BigDecimal("2299"))
                        .stock(36)
                        .category("Fashion")
                        .imageUrl("https://images.unsplash.com/photo-1542291026-7eec264c27ff?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Classic Sunglasses")
                        .description("Classic sunglasses with a lightweight frame and stylish everyday design.")
                        .price(new BigDecimal("999"))
                        .stock(45)
                        .category("Fashion")
                        .imageUrl("https://images.unsplash.com/photo-1511499767150-a48a237f0083?auto=format&fit=crop&w=800&q=80")
                        .build(),

                // =========================
                // HOME
                // =========================

                Product.builder()
                        .name("Ceramic Coffee Mug")
                        .description("Minimal ceramic coffee mug perfect for tea, coffee and everyday use.")
                        .price(new BigDecimal("399"))
                        .stock(80)
                        .category("Home")
                        .imageUrl("https://images.unsplash.com/photo-1514228742587-6b1558fcca3d?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Table Lamp")
                        .description("Modern table lamp with a simple design that fits bedrooms, offices and study tables.")
                        .price(new BigDecimal("1299"))
                        .stock(22)
                        .category("Home")
                        .imageUrl("https://images.unsplash.com/photo-1507473885765-e6ed057f782c?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Decorative Plant Pot")
                        .description("Elegant decorative plant pot for adding a fresh look to your home or office.")
                        .price(new BigDecimal("599"))
                        .stock(35)
                        .category("Home")
                        .imageUrl("https://images.unsplash.com/photo-1485955900006-10f4d324d411?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Cotton Cushion")
                        .description("Soft cotton cushion with a simple modern pattern for sofas and chairs.")
                        .price(new BigDecimal("499"))
                        .stock(50)
                        .category("Home")
                        .imageUrl("https://images.unsplash.com/photo-1584100936595-c0654b55a2e2?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Stainless Steel Water Bottle")
                        .description("Reusable stainless steel water bottle with a leak-resistant lid and durable construction.")
                        .price(new BigDecimal("799"))
                        .stock(55)
                        .category("Home")
                        .imageUrl("https://images.unsplash.com/photo-1602143407151-7111542de6e8?auto=format&fit=crop&w=800&q=80")
                        .build(),

                // =========================
                // ACCESSORIES
                // =========================

                Product.builder()
                        .name("Leather Wallet")
                        .description("Compact everyday wallet with multiple card slots and a clean classic design.")
                        .price(new BigDecimal("899"))
                        .stock(42)
                        .category("Accessories")
                        .imageUrl("https://images.unsplash.com/photo-1627123424574-724758594e93?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Travel Backpack")
                        .description("Spacious travel backpack with multiple compartments for work, college and travel.")
                        .price(new BigDecimal("1999"))
                        .stock(31)
                        .category("Accessories")
                        .imageUrl("https://images.unsplash.com/photo-1553062407-98eeb64c6a62?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Phone Case")
                        .description("Slim protective phone case with a lightweight design and raised edge protection.")
                        .price(new BigDecimal("499"))
                        .stock(100)
                        .category("Accessories")
                        .imageUrl("https://images.unsplash.com/photo-1601593346740-925612772716?auto=format&fit=crop&w=800&q=80")
                        .build(),

                Product.builder()
                        .name("Minimalist Wrist Watch")
                        .description("Classic minimalist wrist watch with a clean dial and comfortable strap.")
                        .price(new BigDecimal("1499"))
                        .stock(20)
                        .category("Accessories")
                        .imageUrl("https://images.unsplash.com/photo-1524805444758-089113d48a6d?auto=format&fit=crop&w=800&q=80")
                        .build()
            );

            productRepository.saveAll(products);

            System.out.println(
                    "========================================"
            );

            System.out.println(
                    "20 ShopEase products inserted successfully!"
            );

            System.out.println(
                    "========================================"
            );
        };
    }
}