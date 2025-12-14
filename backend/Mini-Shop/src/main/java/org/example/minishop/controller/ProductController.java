package org.example.minishop.controller;


import org.example.minishop.dto.ProductDto;
import org.example.minishop.service.ProductService;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public List<ProductDto> findAll() {
        return productService.findAllDtos();
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> save(@RequestBody ProductDto product) {
        productService.save(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(product);
    }

    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity saveAll(@RequestBody List<ProductDto> products) {
        try {
            productService.saveAll(products);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(products);
        } catch (IllegalArgumentException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
    }
}
