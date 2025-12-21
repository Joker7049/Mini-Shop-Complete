package com.example.mini_shop_frontend.model

data class ProductDto(
        val id: Long,
        val name: String,
        val description: String,
        val price: Double,
        val quantity: Int,
        val imageUrl: String?
)
