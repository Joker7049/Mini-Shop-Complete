package org.example.minishop.service;

import lombok.extern.slf4j.Slf4j;
import org.example.minishop.dto.ProductDto;
import org.example.minishop.model.Product;
import org.example.minishop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Page<ProductDto> findAllDtos(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        log.debug("Fetching all products");
        return productRepository.findAll(pageable)
                .map(product -> {
                    ProductDto productDto = new ProductDto();
                    productDto.setId(product.getId());
                    productDto.setName(product.getName());
                    productDto.setPrice(product.getPrice());
                    productDto.setQuantity(product.getQuantity());
                    productDto.setDescription(product.getDescription());
                    return productDto;
                });
    }

    public ProductDto save(ProductDto productDto) {
        log.info("Saving new product: {}", productDto.getName());
        Product product = mapToEntity(productDto);
        Product savedProduct = productRepository.save(product);
        log.info("Product saved successfully. ID: {}", savedProduct.getId());

        return mapToDto(savedProduct);
    }

    public List<ProductDto> saveAll(List<ProductDto> productDtos) {
        log.info("Batch saving {} products", productDtos.size());
        List<Product> products = productDtos.stream()
                .map(this::mapToEntity)
                .collect(Collectors.toList());

        List<Product> savedProducts = productRepository.saveAll(products);
        log.info("Batch save completed. Saved {} products", savedProducts.size());

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
