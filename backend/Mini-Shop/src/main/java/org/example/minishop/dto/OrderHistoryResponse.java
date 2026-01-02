package org.example.minishop.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.minishop.model.Status;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OrderHistoryResponse {
    private Long id;
    private String productName;
    private String productImageUrl;
    private Long productId;
    private LocalDateTime orderDate;
    private Status status;
    private double totalPrice;
    private Integer quantity;
}
