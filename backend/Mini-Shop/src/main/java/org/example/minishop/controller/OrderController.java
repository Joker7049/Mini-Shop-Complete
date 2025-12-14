package org.example.minishop.controller;


import org.example.minishop.dto.OrderRequest;
import org.example.minishop.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public ResponseEntity<Map<String, String>> createOrder(@RequestBody OrderRequest orderRequest, Principal principal) {
        try {
            String username = principal.getName();
            orderService.placeOrder(username, orderRequest.getProduct_id(), orderRequest.getCount());
            return ResponseEntity.ok(Collections.singletonMap("message", "Order placed successfully"));

        }catch (Exception ex){
            return  ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("message", ex.getMessage()));
        }
    }

    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders(Principal principal) {
        try {
            List<OrderRequest> orders = orderService.getAllOrders(principal.getName());
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(orders);
        }catch (Exception ex){
            return  ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(ex.getMessage());
        }
    }
}
