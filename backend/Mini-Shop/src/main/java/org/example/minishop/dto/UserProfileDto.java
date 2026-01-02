package org.example.minishop.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileDto {
    private String username;
    private String email;
    private String role;
    private int points;
    private int vouchers;
    private String membershipLevel;
    private int ordersCount;
}
