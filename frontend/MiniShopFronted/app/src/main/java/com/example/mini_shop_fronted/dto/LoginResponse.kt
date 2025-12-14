package com.example.mini_shop_fronted.dto

data class LoginResponse(
    val token: String,
    val username: String,
    val role: String
)
