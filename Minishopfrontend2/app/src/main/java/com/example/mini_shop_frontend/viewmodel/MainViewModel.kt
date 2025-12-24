package com.example.mini_shop_frontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini_shop_frontend.Product
import com.example.mini_shop_frontend.model.LoginRequest
import com.example.mini_shop_frontend.network.ProductResponse
import com.example.mini_shop_frontend.network.RetrofitInstance
import com.example.mini_shop_frontend.utils.UserContext
import com.example.mini_shop_frontend.utils.UserContext.token
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _selectedCategory = MutableStateFlow<String?>(null)
    val selectedCategory: StateFlow<String?> = _selectedCategory.asStateFlow()


    private val _categories = MutableStateFlow<List<String>>(emptyList())
    val categories = _categories.asStateFlow()


    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

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
                // 2. Fetch Categories
                fetchCategories()

                // 3. Fetch Products
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
                var response = ProductResponse(emptyList(), 0, 0, false)
                if (_selectedCategory.value == null) {
                    response = RetrofitInstance.api.getProducts(token)
                } else {
                    response =
                        RetrofitInstance.api.getProductsByCategory(token, _selectedCategory.value!!)
                }

                // Map Backend DTO to UI Model
                val uiProducts =
                    response.content.map { dto ->
                        Product(
                            id = dto.id,
                            name = dto.name,
                            description = dto.description,
                            imageUrl = dto.imageUrl ?: "https://via.placeholder.com/150",
                            rating = dto.rating,
                            price = dto.price,
                            oldPrice = dto.oldPrice,
                            category = dto.category,
                            discountTag = dto.discountTag,
                            isBestSeller = dto.isBestSeller ?: false
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

    private fun fetchCategories() {
        viewModelScope.launch {
            token?.let { token ->
                _categories.value = RetrofitInstance.api.getCategories(token)
            }
        }
    }
    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        fetchProducts()
    }
}
