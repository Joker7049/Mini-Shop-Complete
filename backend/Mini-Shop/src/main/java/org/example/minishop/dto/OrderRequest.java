package org.example.minishop.dto;

import lombok.Data;


@Data
public class OrderRequest {
    private Long product_id;
    private Integer count;
}
