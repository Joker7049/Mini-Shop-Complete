package com.example.mini_shop_frontend.network

import com.example.mini_shop_frontend.model.LoginRequest
import com.example.mini_shop_frontend.model.LoginResponse
import com.example.mini_shop_frontend.model.ProductDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
        @POST("auth/login") suspend fun login(@Body request: LoginRequest): LoginResponse

        @GET("api/products")
        suspend fun getProducts(
                @Header("Authorization") token: String,
                @Query("page") page: Int = 0,
                @Query("size") size: Int = 20
        ): ProductResponse
}

data class ProductResponse(
        val content: List<ProductDto>,
        val totalPages: Int,
        val totalElements: Long,
        val last: Boolean
)
