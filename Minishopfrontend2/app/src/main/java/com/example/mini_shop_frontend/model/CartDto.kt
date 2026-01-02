package com.example.mini_shop_frontend.model

data class CartDto(
    val id: Long,
    val items: List<CartItemDto>,
    val subTotal: Double,
    val shipping: Double,
    val total: Double
)


