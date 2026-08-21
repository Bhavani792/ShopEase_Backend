package com.shopease.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.shopease.dto.OrderResponse;
import com.shopease.dto.OrderStatusRequest;
import com.shopease.service.AdminOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/admin/orders")
@Validated
@PreAuthorize("hasRole('ADMIN')")
public class AdminOrderController {

    private final AdminOrderService adminOrderService;

    public AdminOrderController(
            AdminOrderService adminOrderService) {

        this.adminOrderService = adminOrderService;
    }

    @GetMapping
    public ResponseEntity<List<OrderResponse>> getAllOrders() {

        return ResponseEntity.ok(
                adminOrderService.getAllOrders()
        );
    }

    @PutMapping("/{orderId}/status")
    public ResponseEntity<OrderResponse> updateOrderStatus(
            @PathVariable Long orderId,
            @Valid @RequestBody OrderStatusRequest request) {

        return ResponseEntity.ok(
                adminOrderService.updateOrderStatus(
                        orderId,
                        request
                )
        );
    }
}