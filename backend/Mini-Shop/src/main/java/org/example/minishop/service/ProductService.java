package org.example.minishop.service;


import org.example.minishop.dto.ProductDto;
import org.example.minishop.model.Product;
import org.example.minishop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> findAllDtos() {
        return productRepository.findAll()
                .stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ProductDto save(ProductDto productDto) {
        Product product = mapToEntity(productDto);
        Product savedProduct = productRepository.save(product);

        return mapToDto(savedProduct);
    }

    public List<ProductDto> saveAll(List<ProductDto> productDtos) {
        List<Product> products = productDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        List<Product> savedProducts = productRepository.saveAll(products);

        return savedProducts.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }



    private ProductDto mapToDto(Product product) {
        ProductDto dto = new ProductDto();
        dto.setId(product.getId());
        dto.setName(product.getName());
        dto.setDescription(product.getDescription());
        dto.setPrice(product.getPrice());
        dto.setQuantity(product.getQuantity());
        return dto;
    }

    private Product mapToEntity(ProductDto dto) {
        Product product = new Product();
        product.setName(dto.getName());
        product.setDescription(dto.getDescription());
        product.setPrice(dto.getPrice());
        product.setQuantity(dto.getQuantity());
        return product;
    }
}
