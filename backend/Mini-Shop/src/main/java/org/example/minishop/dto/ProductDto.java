package org.example.minishop.dto;


import ch.qos.logback.core.util.StringUtil;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.URL;

@Data
public class ProductDto {
    private Long id;

    @NotBlank(message = "Product name is required")
    @Size(min = 3, max = 100, message = "Name must be between 3 and 100 characters")
    private String name;

    @Size(max = 500, message = "Description is too long")
    private String description;

    @NotNull(message = "Price is required")
    @Positive(message = "Price must be greater than zero")
    private Double price;

    @NotNull(message = "Quantity is required")
    @Min(value = 0, message = "Quantity must be at least 1")
    private Integer quantity;

    @URL(protocol = "http", host = "127.0.0.1", message = "Image URL must be a valid URL starting with http://")
    private String imageUrl;
}
