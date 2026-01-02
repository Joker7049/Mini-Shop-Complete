package org.example.minishop.controller;


import lombok.RequiredArgsConstructor;
import lombok.val;
import org.example.minishop.model.Cart;
import org.example.minishop.service.CartService;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/cart")
@RequiredArgsConstructor
public class CartController {
    private final CartService cartService;

    @GetMapping
    public Cart getCart(Principal principal) {
        val username = principal.getName();
        return cartService.getCart(username);
    }

    @PostMapping
    public Cart addToCart(Principal principal, @RequestParam Long productId, @RequestParam Integer quantity) {
        val username = principal.getName();
        return cartService.addToCart(username, productId, quantity);
    }

    @DeleteMapping("/{productId}")
    public Cart removeFromCart(Principal principal, @PathVariable Long productId) {
        val username = principal.getName();
        return cartService.removeFromCart(username, productId);
    }

    @DeleteMapping
    public Cart clearCart(Principal principal) {
        val username = principal.getName();
        return cartService.clearCart(username);
    }


}
