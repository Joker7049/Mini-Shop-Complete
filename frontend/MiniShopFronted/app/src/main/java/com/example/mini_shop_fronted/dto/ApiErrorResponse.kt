package com.example.mini_shop_fronted.dto



data class ApiErrorResponse(
    val timestamp: String?,
    val status: Int?,
    val error: String?,
    val message: String?,
    val path: String?,
    val validationErrors: Map<String, String>?
)
