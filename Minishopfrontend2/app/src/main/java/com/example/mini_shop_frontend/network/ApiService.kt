package com.example.mini_shop_frontend.network

import com.example.mini_shop_frontend.model.CartDto
import com.example.mini_shop_frontend.model.LoginRequest
import com.example.mini_shop_frontend.model.LoginResponse
import com.example.mini_shop_frontend.model.OrderHistoryDto
import com.example.mini_shop_frontend.model.ProductDto
import com.example.mini_shop_frontend.model.SignUpRequest
import com.example.mini_shop_frontend.model.UserProfileDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
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
        ): Response<ProductResponse>

        @GET("api/products/category/{categoryName}")
        suspend fun getProductsByCategory(
                @Header("Authorization") token: String,
                @Path("categoryName") categoryName: String,
                @Query("page") page: Int = 0,
                @Query("size") size: Int = 20
        ): Response<ProductResponse>

        @GET("api/categories")
        suspend fun getCategories(@Header("Authorization") token: String): Response<List<String>>

        @GET("api/users/me")
        suspend fun getUserProfile(@Header("Authorization") token: String): Response<UserProfileDto>

        @GET("api/orders/my-orders")
        suspend fun getMyOrders(
                @Header("Authorization") token: String
        ): Response<List<OrderHistoryDto>>

        @GET("api/cart")
        suspend fun getCart(@Header("Authorization") token: String): Response<CartDto>

        @POST("api/cart")
        suspend fun addToCart(
                @Header("Authorization") token: String,
                @Query("productId") productId: Long,
                @Query("quantity") quantity: Int
        ): Response<CartDto>

        @DELETE("api/cart/{productId}")
        suspend fun removeFromCart(
                @Header("Authorization") token: String,
                @Path("productId") productId: Long
        ): Response<CartDto>

        @DELETE("api/cart")
        suspend fun clearCart(@Header("Authorization") token: String): Response<CartDto>
        @POST("api/orders/checkout")
        suspend fun checkout(@Header("Authorization") token: String): Response<Map<String, String>>
}

data class ProductResponse(
        val content: List<ProductDto>,
        val totalPages: Int,
        val totalElements: Long,
        val last: Boolean
)
