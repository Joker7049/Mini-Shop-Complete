package org.example.minishop.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Data;


@Data
public class OrderRequest {

    @NotNull(message = "Product ID is required")
    private Long product_id;

    @NotNull(message = "Count is required")
    @Min(value = 1, message = "The count must be at least 1")
    private Integer count;
}
