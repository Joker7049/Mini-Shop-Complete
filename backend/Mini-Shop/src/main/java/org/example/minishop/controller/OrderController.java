package org.example.minishop.controller;

import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.example.minishop.dto.OrderHistoryResponse;
import org.example.minishop.dto.OrderRequest;
import org.example.minishop.service.OrderService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
@Slf4j
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            Principal principal) {

        log.info("Received request to create order from user: {}", principal.getName());
        orderService.placeOrder(
                principal.getName(),
                orderRequest.getProduct_id(),
                orderRequest.getCount());

        return ResponseEntity.ok(Collections.singletonMap("message", "Order placed successfully"));
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, String>> checkout(Principal principal) {
        log.info("Received request to checkout for user: {}", principal.getName());
        orderService.checkout(principal.getName());
        return ResponseEntity.ok(Collections.singletonMap("message", "Checkout successful"));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderHistoryResponse>> getMyOrders(Principal principal) {
        List<OrderHistoryResponse> orders = orderService.getAllOrders(principal.getName());
        return ResponseEntity.ok(orders);
    }
}
