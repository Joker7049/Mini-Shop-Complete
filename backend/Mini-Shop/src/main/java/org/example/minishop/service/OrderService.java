package org.example.minishop.service;



import jakarta.transaction.Transactional;
import org.example.minishop.dto.OrderRequest;
import org.example.minishop.exception.BadRequestException;
import org.example.minishop.exception.ResourceNotFoundException;
import org.example.minishop.model.Order;
import org.example.minishop.model.Product;
import org.example.minishop.model.Status;
import org.example.minishop.model.User;
import org.example.minishop.repository.OrderRepository;
import org.example.minishop.repository.ProductRepository;
import org.example.minishop.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    public OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.productRepository = productRepository;
    }

    @Transactional
    public void placeOrder(String username, Long productId, Integer quantity) {
        // 1. Find Product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found with ID: " + productId));

        // 2. Find User
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

        // 3. Check Inventory
        if (product.getQuantity() < quantity) {
            throw new BadRequestException("Not enough inventory. Available: " + product.getQuantity());
        }

        // 4. Update Inventory
        product.setQuantity(product.getQuantity() - quantity);


        // 5. Create Order
        Order order = new Order();
        order.setUser(user);
        order.setProduct(product);
        order.setOrder_date(LocalDateTime.now());
        order.setCount(quantity);
        order.setStatus(Status.PENDING);
        order.setTotal_price(product.getPrice() * quantity);

        orderRepository.save(order);
    }

    @Transactional
    public List<OrderRequest> getAllOrders(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));


        return user.getOrders().stream()
                .map(order -> {
                    OrderRequest req = new OrderRequest();
                    req.setProduct_id(order.getProduct().getId());
                    req.setCount(order.getCount());
                    return req;
                })
                .collect(Collectors.toList());
    }
}
