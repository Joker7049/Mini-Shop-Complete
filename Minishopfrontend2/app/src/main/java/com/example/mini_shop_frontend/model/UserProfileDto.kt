package com.example.mini_shop_frontend.model

data class UserProfileDto(
        val username: String,
        val email: String?,
        val role: String,
        val points: Int,
        val vouchers: Int,
        val membershipLevel: String,
        val ordersCount: Int
)
