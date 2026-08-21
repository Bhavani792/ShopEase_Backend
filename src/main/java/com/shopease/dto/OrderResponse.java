package com.shopease.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.shopease.entity.OrderStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderResponse {

    private Long id;
    private BigDecimal totalAmount;
    private OrderStatus status;
    private LocalDateTime orderDate;

    private UserResponse user;

    private List<OrderItemResponse> items;
}