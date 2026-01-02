package com.example.mini_shop_frontend.model

data class CartItemDto(
    val id: Long,
    val product: ProductDto,
    val quantity: Int,
)

