package com.example.mini_shop_frontend.model



data class OrderHistoryDto(
    val id: Long,
    val productName: String,
    val productImageUrl: String,
    val productId: Long,
    val orderDate: String,
    val status: String,
    val totalPrice: Double,
    val quantity: Int
)
