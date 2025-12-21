package com.example.mini_shop_fronted.dto



// Generic wrapper for Spring's Page object
data class PageResponse<T>(
    val content: List<T>,
    val totalElements: Long,
    val totalPages: Int,
    val size: Int,
    val number: Int, // Current page index (starts at 0)
    val last: Boolean,
    val first: Boolean
)
