package org.example.minishop.repository;

import org.example.minishop.model.Product;
import org.example.minishop.util.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,Long> {

    Page<Product> findByCategory(Pageable pageable, Category category);
}
