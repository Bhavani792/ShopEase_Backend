package com.shopease.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.shopease.dto.CartItemResponse;
import com.shopease.service.CartService;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(
            Authentication authentication,
            @RequestParam Long productId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartService.addToCart(
                        authentication.getName(),
                        productId,
                        quantity
                )
        );
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(
            Authentication authentication) {

        return ResponseEntity.ok(
                cartService.getCart(
                        authentication.getName()
                )
        );
    }

    @PutMapping("/update/{cartItemId}")
    public ResponseEntity<String> updateQuantity(
            Authentication authentication,
            @PathVariable Long cartItemId,
            @RequestParam Integer quantity) {

        return ResponseEntity.ok(
                cartService.updateQuantity(
                        authentication.getName(),
                        cartItemId,
                        quantity
                )
        );
    }

    @DeleteMapping("/remove/{cartItemId}")
    public ResponseEntity<String> removeFromCart(
            Authentication authentication,
            @PathVariable Long cartItemId) {

        return ResponseEntity.ok(
                cartService.removeFromCart(
                        authentication.getName(),
                        cartItemId
                )
        );
    }
}