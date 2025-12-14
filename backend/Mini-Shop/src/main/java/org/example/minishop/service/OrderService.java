package org.example.minishop.service;


import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import org.example.minishop.dto.OrderRequest;
import org.example.minishop.model.Order;
import org.example.minishop.model.Product;
import org.example.minishop.model.Status;
import org.example.minishop.model.User;
import org.example.minishop.repository.OrderRepository;
import org.example.minishop.repository.ProductRepository;
import org.example.minishop.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProductRepository productRepository;

    @Transactional
    public OrderRequest placeOrder(String username, Long product_id, Integer quantity) {
        Product product = productRepository.findById(product_id).orElseThrow(() -> new EntityNotFoundException("Product not found"));
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));

        if (product.getQuantity() < quantity) {
            throw new RuntimeException("Not enough inventory");
        }
        product.setQuantity(product.getQuantity() - quantity);
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setOrder_date(LocalDateTime.now());
        order.setCount(quantity);
        order.setStatus(Status.PENDING);
        order.setTotal_price(product.getPrice() * quantity);
        orderRepository.save(order);
        OrderRequest orderRequest = new OrderRequest();
        orderRequest.setProduct_id(product_id);
        orderRequest.setCount(quantity);
        return orderRequest;
    }


    public List<OrderRequest> getAllOrders(String username) {
        User user = userRepository.findByUsername(username).orElseThrow(() -> new EntityNotFoundException("User not found"));
        List<Order> orders = user.getOrders();
        List<OrderRequest> orderRequests = new ArrayList<>();
        for (Order order : orders) {
            OrderRequest orderRequest = new OrderRequest();
            orderRequest.setProduct_id(order.getProduct().getId());
            orderRequest.setCount(order.getCount());
            orderRequests.add(orderRequest);
        }
        return orderRequests;

    }
}
