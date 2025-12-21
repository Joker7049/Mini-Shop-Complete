package com.example.mini_shop_fronted.api

import com.example.mini_shop_fronted.dto.DeleteUserResponse
import com.example.mini_shop_fronted.dto.LoginRequest
import com.example.mini_shop_fronted.dto.LoginResponse
import com.example.mini_shop_fronted.dto.OllamaProductDescriptionResponse
import com.example.mini_shop_fronted.dto.Order
import com.example.mini_shop_fronted.dto.OrderResponse
import com.example.mini_shop_fronted.dto.PageResponse
import com.example.mini_shop_fronted.dto.Product
import com.example.mini_shop_fronted.dto.SignUpRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path

interface MiniShopApi {

    // 1. Login (no token needed)
    @POST("auth/login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>


    // 2. Register
    @POST("auth/register")
    suspend fun register(@Body signUpRequest: SignUpRequest): Response<LoginResponse>


    // 3. Get products (needs token)
    @GET("api/products")
    suspend fun getProducts(@Header("Authorization") token: String): Response<PageResponse<Product>>


    // 4. Delete user (needs token and path variable and the urse's role must be "ADMIN")
    @DELETE("api/users/{username}")
    suspend fun deleteUser(
        @Header("Authorization") token: String,
        @Path("username") username: String
    ): Response<DeleteUserResponse>


    // 5. place an order (needs token and a order object)
    @POST("api/orders")
    suspend fun placeOrder(
        @Header("Authorization") token: String,
        @Body order: Order
    ): Response<OrderResponse>


    // 6. adding a product (needs a token and the user's role must be "ADMIN")
    @POST("api/products")
    suspend fun addProduct(
        @Header("Authorization") token: String,
        @Body product: Product
    ): Response<Product>


    @GET("ollama/product-description/{productName}")
    suspend fun getAiDescription(
        @Header("Authorization") token: String,
        @Path("productName") productName: String
    ): Response<OllamaProductDescriptionResponse>



}