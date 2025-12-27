package com.example.mini_shop_frontend.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini_shop_frontend.Product
import com.example.mini_shop_frontend.model.LoginRequest
import com.example.mini_shop_frontend.model.LoginResponse
import com.example.mini_shop_frontend.model.SignUpRequest
import com.example.mini_shop_frontend.model.UserProfileDto
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

    private val _userProfile = MutableStateFlow<UserProfileDto?>(null)
    val userProfile: StateFlow<UserProfileDto?> = _userProfile.asStateFlow()

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response = RetrofitInstance.api.login(LoginRequest(username, password))
                if (response.isSuccessful) {
                    _isLoading.value = false
                    handleAuthSuccess(response.body()!!)
                } else {
                    _isLoading.value = false
                    _error.value = "Login failed: ${response.errorBody().toString()}"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Login failed: ${e.message}"
            }
        }
    }

    fun signUp(username: String, password: String, email: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                val response =
                        RetrofitInstance.api.register(SignUpRequest(username, password, email))
                if (response.isSuccessful) {
                    _isLoading.value = false
                    handleAuthSuccess(response.body()!!)
                } else {
                    _isLoading.value = false
                    _error.value = "Sign up failed: ${response.errorBody()?.string()}"
                }
            } catch (e: Exception) {
                _isLoading.value = false
                _error.value = "Login failed: ${e.message}"
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
                            RetrofitInstance.api.getProductsByCategory(
                                    token,
                                    _selectedCategory.value!!
                            )
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
            token?.let { token -> _categories.value = RetrofitInstance.api.getCategories(token) }
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                token?.let { token ->
                    _userProfile.value = RetrofitInstance.api.getUserProfile(token)
                }
            } catch (e: Exception) {
                Log.e("ProfileError", "Failed to fetch user profile", e)
                e.printStackTrace()
            }
        }
    }

    private fun handleAuthSuccess(response: LoginResponse) {
        UserContext.setUserData(response.token, response.username, response.role)
        _isLoggedIn.value = true
        fetchCategories()
        fetchUserProfile()
        fetchProducts()
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        fetchProducts()
    }

    fun logout() {
        UserContext.clear()
        _isLoggedIn.value = false
    }
}
