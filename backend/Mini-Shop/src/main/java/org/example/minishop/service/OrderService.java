package org.example.minishop.service;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.example.minishop.dto.OrderHistoryResponse;
import org.example.minishop.exception.BadRequestException;
import org.example.minishop.exception.ResourceNotFoundException;
import org.example.minishop.model.Cart;
import org.example.minishop.model.CartItem;
import org.example.minishop.model.Order;
import org.example.minishop.model.OrderItem;
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
@Slf4j
public class OrderService {

        private final OrderRepository orderRepository;
        private final UserRepository userRepository;
        private final ProductRepository productRepository;
        private final CartService cartService;

        public OrderService(OrderRepository orderRepository, UserRepository userRepository,
                        ProductRepository productRepository, CartService cartService) {
                this.orderRepository = orderRepository;
                this.userRepository = userRepository;
                this.productRepository = productRepository;
                this.cartService = cartService;
        }

        @Transactional
        public void checkout(String username) {
                log.info("Request to checkout for user: {}", username);
                Cart cart = cartService.getCart(username);
                if (cart.getItems().isEmpty()) {
                        log.warn("Checkout failed. Cart is empty for user: {}", username);
                        throw new BadRequestException("Cannot checkout an empty cart");
                }

                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

                // Create one Order Header
                Order order = new Order();
                order.setUser(user);
                order.setOrder_date(LocalDateTime.now());
                order.setStatus(Status.PENDING);
                order.setTotal_price(cart.getTotal());

                for (CartItem cartItem : cart.getItems()) {
                        Product product = cartItem.getProduct();
                        int quantity = cartItem.getQuantity();

                        // Check Inventory
                        if (product.getQuantity() < quantity) {
                                throw new BadRequestException("Not enough inventory for " + product.getName());
                        }

                        // Update Inventory
                        product.setQuantity(product.getQuantity() - quantity);

                        // Create Order Item
                        OrderItem orderItem = new OrderItem();
                        orderItem.setOrder(order);
                        orderItem.setProduct(product);
                        orderItem.setQuantity(quantity);
                        orderItem.setPrice(product.getPrice());
                        order.getItems().add(orderItem);
                }

                orderRepository.save(order);
                cartService.clearCart(username);
                log.info("Checkout completed successfully. Order ID: {}", order.getId());
        }

        @Transactional
        public void placeOrder(String username, Long productId, Integer quantity) {
                log.info("Request to place order. User: {}, ProductId: {}, Quantity: {}", username, productId,
                                quantity);

                Product product = productRepository.findById(productId)
                                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

                if (product.getQuantity() < quantity) {
                        throw new BadRequestException("Not enough inventory");
                }

                product.setQuantity(product.getQuantity() - quantity);

                Order order = new Order();
                order.setUser(user);
                order.setOrder_date(LocalDateTime.now());
                order.setStatus(Status.PENDING);
                order.setTotal_price(product.getPrice() * quantity);

                OrderItem item = new OrderItem();
                item.setOrder(order);
                item.setProduct(product);
                item.setQuantity(quantity);
                item.setPrice(product.getPrice());
                order.getItems().add(item);

                orderRepository.save(order);
        }

        @Transactional
        public List<OrderHistoryResponse> getAllOrders(String username) {
                User user = userRepository.findByUsername(username)
                                .orElseThrow(() -> new ResourceNotFoundException("User not found: " + username));

                // Flatten all OrderItems from all Orders into a list of responses
                return user.getOrders().stream()
                                .flatMap(order -> order.getItems().stream().map(item -> {
                                        return OrderHistoryResponse.builder()
                                                        .id((long) order.getId())
                                                        .productId(item.getProduct().getId())
                                                        .productName(item.getProduct().getName())
                                                        .orderDate(order.getOrder_date())
                                                        .productImageUrl(item.getProduct().getImageUrl())
                                                        .status(order.getStatus())
                                                        .quantity(item.getQuantity())
                                                        .totalPrice(item.getPrice() * item.getQuantity())
                                                        .build();
                                }))
                                .collect(Collectors.toList());
        }
}
