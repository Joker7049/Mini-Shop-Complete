package org.example.minishop.controller;


import jakarta.validation.Valid;
import org.example.minishop.dto.ProductDto;
import org.example.minishop.service.ProductService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public ResponseEntity<Page<ProductDto>> findAll(@RequestParam(defaultValue = "0") Integer page, @RequestParam(defaultValue = "0") Integer size) {
        return ResponseEntity.ok(productService.findAllDtos(page, size));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> save(@Valid @RequestBody ProductDto product) {
        ProductDto savedProduct = productService.save(product);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProduct);
    }

    @PostMapping("/batch")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ProductDto>> saveAll(@RequestBody @Valid List<ProductDto> products) {

        List<ProductDto> savedProducts = productService.saveAll(products);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(savedProducts);
    }
}
