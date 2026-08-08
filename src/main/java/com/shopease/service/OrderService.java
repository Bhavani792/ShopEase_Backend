package com.shopease.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.shopease.dto.OrderItemResponse;
import com.shopease.dto.OrderResponse;
import com.shopease.dto.UserResponse;
import com.shopease.entity.CartItem;
import com.shopease.entity.Order;
import com.shopease.entity.OrderItem;
import com.shopease.entity.OrderStatus;
import com.shopease.entity.Product;
import com.shopease.entity.User;
import com.shopease.repository.CartItemRepository;
import com.shopease.repository.OrderItemRepository;
import com.shopease.repository.OrderRepository;
import com.shopease.repository.ProductRepository;
import com.shopease.repository.UserRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public OrderService(
            OrderRepository orderRepository,
            OrderItemRepository orderItemRepository,
            CartItemRepository cartItemRepository,
            ProductRepository productRepository,
            UserRepository userRepository) {

        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public OrderResponse checkout(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        List<CartItem> cartItems =
                cartItemRepository.findByUser(user);

        if (cartItems.isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        BigDecimal totalAmount = BigDecimal.ZERO;

        List<OrderItem> orderItems = new ArrayList<>();

        for (CartItem cartItem : cartItems) {

            Product product = cartItem.getProduct();

            if (product.getStock() < cartItem.getQuantity()) {
                throw new RuntimeException(
                        "Insufficient stock for product: "
                                + product.getName());
            }

            BigDecimal itemTotal =
                    product.getPrice()
                            .multiply(
                                    BigDecimal.valueOf(
                                            cartItem.getQuantity()
                                    )
                            );

            totalAmount = totalAmount.add(itemTotal);

            product.setStock(
                    product.getStock()
                            - cartItem.getQuantity()
            );

            productRepository.save(product);

            OrderItem orderItem = OrderItem.builder()
                    .quantity(cartItem.getQuantity())
                    .price(product.getPrice())
                    .product(product)
                    .build();

            orderItems.add(orderItem);
        }

        Order order = Order.builder()
                .user(user)
                .totalAmount(totalAmount)
                .status(OrderStatus.PLACED)
                .orderDate(LocalDateTime.now())
                .build();

        Order savedOrder = orderRepository.save(order);

        for (OrderItem orderItem : orderItems) {

            orderItem.setOrder(savedOrder);

            orderItemRepository.save(orderItem);
        }

        cartItemRepository.deleteAll(cartItems);

        return convertToResponse(savedOrder);
    }

    public List<OrderResponse> getMyOrders(String email) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        return orderRepository
                .findByUserOrderByOrderDateDesc(user)
                .stream()
                .map(this::convertToResponse)
                .toList();
    }

    public OrderResponse getMyOrder(
            String email,
            Long orderId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You cannot access another user's order");
        }

        return convertToResponse(order);
    }

    @Transactional
    public String cancelOrder(
            String email,
            Long orderId) {

        User user = userRepository.findByEmail(email)
                .orElseThrow(() ->
                        new RuntimeException("User not found"));

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new RuntimeException("Order not found"));

        if (!order.getUser().getId().equals(user.getId())) {
            throw new RuntimeException(
                    "You cannot cancel another user's order");
        }

        if (order.getStatus() == OrderStatus.CANCELLED) {
            throw new RuntimeException(
                    "Order is already cancelled");
        }

        if (order.getStatus() != OrderStatus.PLACED) {
            throw new RuntimeException(
                    "Order cannot be cancelled now");
        }

        order.setStatus(OrderStatus.CANCELLED);

        orderRepository.save(order);

        return "Order cancelled successfully";
    }

    private OrderResponse convertToResponse(Order order) {

        User user = order.getUser();

        UserResponse userResponse = UserResponse.builder()
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
                        .map(item ->
                                OrderItemResponse.builder()
                                        .id(item.getId())
                                        .productId(
                                                item.getProduct().getId())
                                        .productName(
                                                item.getProduct().getName())
                                        .quantity(
                                                item.getQuantity())
                                        .price(
                                                item.getPrice())
                                        .build()
                        )
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
}