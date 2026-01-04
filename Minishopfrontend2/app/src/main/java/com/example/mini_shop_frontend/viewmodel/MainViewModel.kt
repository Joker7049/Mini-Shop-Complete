package com.example.mini_shop_frontend.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini_shop_frontend.Product
import com.example.mini_shop_frontend.model.LoginRequest
import com.example.mini_shop_frontend.model.LoginResponse
import com.example.mini_shop_frontend.model.OrderHistoryDto
import com.example.mini_shop_frontend.model.ProductDto
import com.example.mini_shop_frontend.model.SignUpRequest
import com.example.mini_shop_frontend.model.UserProfileDto
import com.example.mini_shop_frontend.network.RetrofitInstance
import com.example.mini_shop_frontend.utils.TokenManager
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

    private val _searchQuery = MutableStateFlow<String>("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()

    private val _maxPrice = MutableStateFlow<Double?>(null)
    val maxPrice: StateFlow<Double?> = _maxPrice.asStateFlow()


    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _orders = MutableStateFlow<List<OrderHistoryDto>>(emptyList())
    val orders = _orders.asStateFlow()

    init {
        TokenManager.getToken()?.let { token ->
            UserContext.setUserData(token, "", "")
            _isLoggedIn.value = true
            fetchAllData()
        }
    }

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

    fun fetchOrderHistory() {
        viewModelScope.launch {
            try {
                UserContext.token?.let { token ->
                    val response = RetrofitInstance.api.getMyOrders(token)
                    if (response.isSuccessful) {
                        _orders.value = response.body() ?: emptyList()
                    }
                }
            } catch (e: Exception) {
                Log.e("OrderHistory", "Failed to fetch orders", e)
                // We don't want to show a big error message for history usually,
                // but let's log it.
            }
        }
    }

    fun fetchProducts() {
        viewModelScope.launch {
            // _isLoading.value = true // Already loading from loginAndFetch
            try {
                val token = UserContext.token ?: throw Exception("Not Logged In")
                val response =
                    if (_selectedCategory.value == null) {
                        RetrofitInstance.api.getProducts(token)
                    } else {
                        RetrofitInstance.api.getProductsByCategory(
                            token,
                            _selectedCategory.value!!
                        )
                    }

                if (response.isSuccessful) {
                    val content = response.body()?.content ?: emptyList()
                    // Map Backend DTO to UI Model
                    val uiProducts =
                        content.map { dto ->
                            mapProductDtoToUiModel(dto)
                        }
                    _products.value = uiProducts
                    _error.value = null
                } else {
                    _error.value = "Failed to load products: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Failed to load products: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun searchProducts(query: String) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _searchQuery.value = query
                val token = UserContext.token ?: throw Exception("Not Logged In")
                val response = RetrofitInstance.api.searchByKeyword(token, query)
                if (response.isSuccessful) {
                    val content = response.body()?.content ?: emptyList()
                    _products.value = content.map { dto ->
                        mapProductDtoToUiModel(dto)
                    }
                    _error.value = null
                } else {
                    _error.value = "Failed to load products: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = "Failed to load products: ${e.message}"
                e.printStackTrace()
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun filterProducts(maxPrice: Double) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _maxPrice.value = maxPrice
                val token = UserContext.token ?: throw Exception("Not Logged In")
                val response = RetrofitInstance.api.getProductsByFilter(token, maxPrice)
                if (response.isSuccessful) {
                    val content = response.body()?.content ?: emptyList()
                    _products.value = content.map { dto ->
                        mapProductDtoToUiModel(dto)
                    }
                    _error.value = null
                } else {
                    _error.value = "Failed to load products: ${response.code()}"
                }

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
            try {
                UserContext.token?.let { token ->
                    val response = RetrofitInstance.api.getCategories(token)
                    if (response.isSuccessful) {
                        _categories.value = response.body() ?: emptyList()
                    }
                }
            } catch (e: Exception) {
                Log.e("Categories", "Failed to fetch categories", e)
            }
        }
    }

    private fun fetchUserProfile() {
        viewModelScope.launch {
            try {
                token?.let { token ->
                    val response = RetrofitInstance.api.getUserProfile(token)
                    if (response.isSuccessful) {
                        val profile = response.body()!!
                        _userProfile.value = profile

                        UserContext.setUserData(
                            token.removePrefix("Bearer "),
                            profile.username,
                            profile.role
                        )
                    }
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
        TokenManager.saveToken(response.token)
        fetchAllData()
    }

    fun selectCategory(category: String?) {
        _selectedCategory.value = category
        fetchProducts()
    }

    fun logout() {
        UserContext.clear()
        TokenManager.clearToken()
        _isLoggedIn.value = false
        _orders.value = emptyList()
        _userProfile.value = null
    }

    private fun fetchAllData() {
        fetchCategories()
        fetchProducts()
        fetchUserProfile()
        fetchOrderHistory()
    }

    private fun mapProductDtoToUiModel(dto: ProductDto): Product {
        return Product(
            id = dto.id,
            name = dto.name,
            description = dto.description,
            imageUrl = dto.imageUrl
                ?: "https://via.placeholder.com/150",
            rating = dto.rating,
            price = dto.price,
            oldPrice = dto.oldPrice,
            category = dto.category,
            discountTag = dto.discountTag,
            isBestSeller = dto.isBestSeller ?: false,
            quantity = dto.quantity
        )
    }
}
