package com.example.mini_shop_fronted

import kotlinx.serialization.Serializable

sealed class Screen {
    @Serializable object Login

    @Serializable object SignUp

    @Serializable object Home

    @Serializable object AdminPanel

    @Serializable object AddProduct

    @Serializable object DeleteUser

    @Serializable
    data class ProductDetail(
            val productId: Long,
            val name: String,
            val description: String,
            val price: Double,
            val quantity: Int
    )
}
