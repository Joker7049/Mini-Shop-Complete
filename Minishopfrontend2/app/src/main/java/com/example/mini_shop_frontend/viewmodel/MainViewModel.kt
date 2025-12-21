package com.example.mini_shop_frontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini_shop_frontend.Product
import com.example.mini_shop_frontend.model.LoginRequest
import com.example.mini_shop_frontend.network.RetrofitInstance
import com.example.mini_shop_frontend.utils.UserContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    init {
        loginAndFetch()
    }

    private fun loginAndFetch() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                // 1. Login (Hardcoded for now)
                val loginResponse = RetrofitInstance.api.login(LoginRequest("asad", "Joker3030"))
                UserContext.setUserData(
                        loginResponse.token,
                        loginResponse.username,
                        loginResponse.role
                )

                // 2. Fetch Products
                fetchProducts()
            } catch (e: Exception) {
                _error.value = "Login Failed: ${e.message}"
                e.printStackTrace()
                _isLoading.value = false
            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            // _isLoading.value = true // Already loading from loginAndFetch
            try {
                val token = UserContext.token ?: throw Exception("Not Logged In")
                val response = RetrofitInstance.api.getProducts(token, page = 0, size = 20)
                // Map Backend DTO to UI Model
                val uiProducts =
                        response.content.map { dto ->
                            Product(
                                    name = dto.name,
                                    imageUrl = dto.imageUrl ?: "https://via.placeholder.com/150",
                                    rating = 4.5, // Default mock value
                                    price = dto.price,
                                    oldPrice = null,
                                    category = "General", // Default mock value
                                    discountTag = null,
                                    isBestSeller = false
                            )
                        }
                _products.value = uiProducts
                _error.value = null
            } catch (e: Exception) {
                _error.value = "Failed to load products: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }
}
