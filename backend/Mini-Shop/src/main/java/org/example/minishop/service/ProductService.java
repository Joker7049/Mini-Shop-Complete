package org.example.minishop.service;


import org.example.minishop.dto.ProductDto;
import org.example.minishop.model.Product;
import org.example.minishop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class ProductService {
    @Autowired
    private ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public List<ProductDto> findAllDtos() {
        List<Product> products = productRepository.findAll();
        List<ProductDto> productDtos = new ArrayList<>();
        for (Product product : products) {
            ProductDto productDto = new ProductDto();
            productDto.setId(product.getId());
            productDto.setDescription(product.getDescription());
            productDto.setPrice(product.getPrice());
            productDto.setQuantity(product.getQuantity());
            productDto.setName(product.getName());
            productDtos.add(productDto);
        }
        return productDtos;
    }

    public ProductDto save(ProductDto productDto) {
        Product product = new Product();
        product.setName(productDto.getName());
        product.setDescription(productDto.getDescription());
        product.setPrice(productDto.getPrice());
        product.setQuantity(productDto.getQuantity());
        productRepository.save(product);
        return productDto;
    }


    public void saveAll(List<ProductDto> productDtos) {
        try {
            List<Product> products = new ArrayList<>();
            for (ProductDto productDto : productDtos) {
                Product product = new Product();
                product.setName(productDto.getName());
                product.setDescription(productDto.getDescription());
                product.setPrice(productDto.getPrice());
                product.setQuantity(productDto.getQuantity());
                products.add(product);
            }
            productRepository.saveAll(products);
        }catch (IllegalArgumentException e){
            throw new IllegalArgumentException("can't save all products, bad content type");
        }
    }
}
