package org.example.minishop.model;

import jakarta.persistence.*;
import lombok.Data;
import org.example.minishop.util.Category;

@Data
@Entity
@Table(name = "products")
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String description;
    private double price;
    private int quantity;

    @Column(name = "imageUrl")
    private String imageUrl;

    private Double rating;
    private Double oldPrice;
    private Category category;
    private String discountTag;
    private Boolean isBestSeller = false;

}



