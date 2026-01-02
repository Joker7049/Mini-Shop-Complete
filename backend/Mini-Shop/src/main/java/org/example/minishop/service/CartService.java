package org.example.minishop.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.example.minishop.exception.ResourceNotFoundException;
import org.example.minishop.model.Cart;
import org.example.minishop.model.CartItem;
import org.example.minishop.model.Product;
import org.example.minishop.model.User;
import org.example.minishop.repository.CartItemRepository;
import org.example.minishop.repository.CartRepository;
import org.example.minishop.repository.ProductRepository;
import org.example.minishop.repository.UserRepository;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Transactional
    public Cart addToCart(String username, Long productId, int quantity) {
        Cart cart = getOrCreateCart(username);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Product not found"));

        Optional<CartItem> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            existingItem.get().setQuantity(existingItem.get().getQuantity() + quantity);
        } else {
            CartItem cartItem = new CartItem();
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setCart(cart);
            cart.getItems().add(cartItem);
        }

        return cartRepository.save(cart);
    }

    @Transactional
    public Cart getCart(String username) {
        return getOrCreateCart(username);
    }

    @Transactional
    public Cart removeFromCart(String username, Long productId) {
        Cart cart = getOrCreateCart(username);
        cart.getItems().removeIf(item -> item.getProduct().getId().equals(productId));
        return cartRepository.save(cart);
    }

    @Transactional
    public Cart clearCart(String username) {
        Cart cart = getOrCreateCart(username);
        cart.getItems().clear();
        return cartRepository.save(cart);
    }

    private Cart getOrCreateCart(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Cart cart = user.getCart();
        if (cart == null) {
            cart = new Cart();
            cart.setUser(user);
            cart.setItems(new ArrayList<>());
            cart = cartRepository.save(cart);
            user.setCart(cart); // Ensure bidirectional link
        }
        return cart;
    }
}
