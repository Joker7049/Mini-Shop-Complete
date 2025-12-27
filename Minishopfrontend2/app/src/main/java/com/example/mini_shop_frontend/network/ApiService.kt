package com.example.mini_shop_frontend.network

import com.example.mini_shop_frontend.model.LoginRequest
import com.example.mini_shop_frontend.model.LoginResponse
import com.example.mini_shop_frontend.model.ProductDto
import com.example.mini_shop_frontend.model.SignUpRequest
import com.example.mini_shop_frontend.model.UserProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
        @POST("auth/login") suspend fun login(@Body request: LoginRequest): Response<LoginResponse>
        @POST("auth/register")
        suspend fun register(@Body request: SignUpRequest): Response<LoginResponse>

        @GET("api/products")
        suspend fun getProducts(
                @Header("Authorization") token: String,
                @Query("page") page: Int = 0,
                @Query("size") size: Int = 20
        ): ProductResponse

        @GET("api/products/category/{categoryName}")
        suspend fun getProductsByCategory(
                @Header("Authorization") token: String,
                @Path("categoryName") categoryName: String,
                @Query("page") page: Int = 0,
                @Query("size") size: Int = 20
        ): ProductResponse

        @GET("api/categories")
        suspend fun getCategories(@Header("Authorization") token: String): List<String>

        @GET("api/users/me")
        suspend fun getUserProfile(@Header("Authorization") token: String): UserProfileDto
}

data class ProductResponse(
        val content: List<ProductDto>,
        val totalPages: Int,
        val totalElements: Long,
        val last: Boolean
)
