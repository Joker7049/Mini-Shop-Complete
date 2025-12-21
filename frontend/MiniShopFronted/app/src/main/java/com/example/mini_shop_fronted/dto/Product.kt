package com.example.mini_shop_fronted.dto

data class Product(
        val id: Long,
        val name: String,
        val description: String,
        val price: Double,
        val quantity: Int,
        val imageUrl: String?
)
