package com.example.mini_shop_frontend.model

data class ProductDto(
        val id: Long,
        val name: String,
        val description: String?,
        val price: Double,
        val quantity: Int,
        val imageUrl: String?,
        val rating: Double?,
        val oldPrice: Double?,
        val category: String?,
        val discountTag: String?,
        val isBestSeller: Boolean?
)
