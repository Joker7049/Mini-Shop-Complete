package org.example.minishop.service;

import org.example.minishop.dto.OrderRequest;
import org.example.minishop.exception.BadRequestException;
import org.example.minishop.exception.ResourceNotFoundException;
import org.example.minishop.model.Order;
import org.example.minishop.model.Product;
import org.example.minishop.model.User;
import org.example.minishop.repository.OrderRepository;
import org.example.minishop.repository.ProductRepository;
import org.example.minishop.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private OrderService orderService;

    private User user;
    private Product product;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setUsername("testUser");

        product = new Product();
        product.setId(1L);
        product.setName("testProduct");
        product.setQuantity(10);
        product.setPrice(100.0);
    }

    @Test
    void placeOrder_ShouldPlaceOrder_WhenInventoryIsSufficient() {
        // Arrange
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        orderService.placeOrder("testUser", 1L, 5);

        // Assert
        verify(productRepository).findById(1L);
        verify(userRepository).findByUsername("testUser");
        verify(orderRepository).save(any(Order.class));
        assertEquals(5, product.getQuantity()); // Verify inventory reduction
    }

    @Test
    void placeOrder_ShouldThrowException_WhenProductNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder("testUser", 1L, 5));
    }

    @Test
    void placeOrder_ShouldThrowException_WhenUserNotFound() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> orderService.placeOrder("testUser", 1L, 5));
    }

    @Test
    void placeOrder_ShouldThrowException_WhenInventoryInsufficient() {
        product.setQuantity(2); // Set low inventory
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        assertThrows(BadRequestException.class, () -> orderService.placeOrder("testUser", 1L, 5));
    }

    @Test
    void getAllOrders_ShouldReturnOrderRequests_WhenUserExists() {
        // Arrange
        Order order1 = createOrder(1, 2);
        Order order2 = createOrder(2, 3);
        List<Order> orders = List.of(order1, order2);
        user.setOrders(orders);

        when(userRepository.findByUsername("testUser")).thenReturn(Optional.of(user));

        // Act
        List<OrderRequest> result = orderService.getAllOrders("testUser");

        // Assert
        assertEquals(2, result.size());
        assertEquals(1L, result.get(0).getProduct_id());
        assertEquals(2, result.get(0).getCount());
    }

    // Helper method to create orders cleanly
    private Order createOrder(long productId, int count) {
        Product p = new Product();
        p.setId(productId);

        Order o = new Order();
        o.setProduct(p);
        o.setCount(count);
        return o;
    }
}
