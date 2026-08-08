package com.shopease.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopease.dto.OrderItemResponse;
import com.shopease.dto.OrderResponse;
import com.shopease.dto.OrderStatusRequest;
import com.shopease.dto.UserResponse;
import com.shopease.entity.Order;
import com.shopease.entity.OrderItem;
import com.shopease.entity.OrderStatus;
import com.shopease.entity.User;
import com.shopease.repository.OrderItemRepository;
import com.shopease.repository.OrderRepository;

@Service
public class AdminOrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public AdminOrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
    }

    // ==========================================
    // GET ALL ORDERS
    // ADMIN ONLY
    // ==========================================
    public List<OrderResponse> getAllOrders() {

        return orderRepository.findAll()
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    // ==========================================
    // UPDATE ORDER STATUS
    // ADMIN ONLY
    // ==========================================
    @Transactional
    public OrderResponse updateOrderStatus(
            Long orderId,
            OrderStatusRequest request) {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Order not found"));

        OrderStatus currentStatus = order.getStatus();
        OrderStatus newStatus = request.getStatus();

        // ------------------------------------------
        // Validate request
        // ------------------------------------------
        if (newStatus == null) {
            throw new RuntimeException(
                    "Order status is required");
        }

        // ------------------------------------------
        // CANCELLED cannot be changed
        // ------------------------------------------
        if (currentStatus == OrderStatus.CANCELLED) {

            throw new RuntimeException(
                    "Cancelled order cannot be updated");
        }

        // ------------------------------------------
        // DELIVERED cannot be changed
        // ------------------------------------------
        if (currentStatus == OrderStatus.DELIVERED) {

            throw new RuntimeException(
                    "Delivered order cannot be updated");
        }

        // ------------------------------------------
        // PLACED -> CONFIRMED only
        // ------------------------------------------
        if (currentStatus == OrderStatus.PLACED) {

            if (newStatus != OrderStatus.CONFIRMED) {

                throw new RuntimeException(
                        "PLACED order can only move to CONFIRMED");
            }
        }

        // ------------------------------------------
        // CONFIRMED -> SHIPPED only
        // ------------------------------------------
        if (currentStatus == OrderStatus.CONFIRMED) {

            if (newStatus != OrderStatus.SHIPPED) {

                throw new RuntimeException(
                        "CONFIRMED order can only move to SHIPPED");
            }
        }

        // ------------------------------------------
        // SHIPPED -> DELIVERED only
        // ------------------------------------------
        if (currentStatus == OrderStatus.SHIPPED) {

            if (newStatus != OrderStatus.DELIVERED) {

                throw new RuntimeException(
                        "SHIPPED order can only move to DELIVERED");
            }
        }

        // ------------------------------------------
        // Update status
        // ------------------------------------------
        order.setStatus(newStatus);

        Order savedOrder =
                orderRepository.save(order);

        return convertToResponse(savedOrder);
    }

    // ==========================================
    // CONVERT ORDER -> DTO
    // ==========================================
    private OrderResponse convertToResponse(
            Order order) {

        User user = order.getUser();

        UserResponse userResponse =
                UserResponse.builder()
                        .id(user.getId())
                        .name(user.getName())
                        .email(user.getEmail())
                        .role(user.getRole())
                        .build();

        List<OrderItemResponse> itemResponses =
                orderItemRepository.findAll()
                        .stream()
                        .filter(item ->
                                item.getOrder()
                                        .getId()
                                        .equals(order.getId()))
                        .map(this::convertItemToResponse)
                        .toList();

        return OrderResponse.builder()
                .id(order.getId())
                .totalAmount(order.getTotalAmount())
                .status(order.getStatus())
                .orderDate(order.getOrderDate())
                .user(userResponse)
                .items(itemResponses)
                .build();
    }

    // ==========================================
    // CONVERT ORDER ITEM -> DTO
    // ==========================================
    private OrderItemResponse convertItemToResponse(
            OrderItem item) {

        return OrderItemResponse.builder()
                .id(item.getId())
                .productId(
                        item.getProduct().getId()
                )
                .productName(
                        item.getProduct().getName()
                )
                .quantity(
                        item.getQuantity()
                )
                .price(
                        item.getPrice()
                )
                .build();
    }
}