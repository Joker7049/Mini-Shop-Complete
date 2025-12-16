package org.example.minishop.controller;


import jakarta.validation.Valid;
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
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(
            @Valid @RequestBody OrderRequest orderRequest,
            Principal principal) {


        orderService.placeOrder(
                principal.getName(),
                orderRequest.getProduct_id(),
                orderRequest.getCount()
        );

        return ResponseEntity.ok(Collections.singletonMap("message", "Order placed successfully"));
    }

    @GetMapping("/my-orders")
    public ResponseEntity<List<OrderRequest>> getMyOrders(Principal principal) {
        List<OrderRequest> orders = orderService.getAllOrders(principal.getName());
        return ResponseEntity.ok(orders);
    }
}
