package com.example.mini_shop_fronted.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini_shop_fronted.dto.LoginRequest
import com.example.mini_shop_fronted.dto.OllamaProductDescriptionResponse
import com.example.mini_shop_fronted.dto.Order
import com.example.mini_shop_fronted.dto.Product
import com.example.mini_shop_fronted.dto.SignUpRequest
import com.example.mini_shop_fronted.network.RetrofitClient.api
import com.example.mini_shop_fronted.utils.UserContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {

    private val _loginState = MutableStateFlow<LoginState>(LoginState.Idle)
    val loginState: StateFlow<LoginState> = _loginState.asStateFlow()

    private val _productState = MutableStateFlow<ProductState>(ProductState.Loading)
    val productState: StateFlow<ProductState> = _productState.asStateFlow()

    private val _orderState = MutableStateFlow<OrderState>(OrderState.Idle)
    val orderState: StateFlow<OrderState> = _orderState.asStateFlow()

    private val _adminActionState = MutableStateFlow<AdminActionState>(AdminActionState.Idle)
    val adminActionState: StateFlow<AdminActionState> = _adminActionState.asStateFlow()

    private val _ollamaProductDescription =
        MutableStateFlow<OllamaProductDescriptionState>(OllamaProductDescriptionState.Idle)
    val ollamaProductDescriptionResponse = _ollamaProductDescription.asStateFlow()

    fun login(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val loginRequest = LoginRequest(username, password)
                val response = api.login(loginRequest)

                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    if (loginResponse != null) {
                        UserContext.setUserData(
                            token = loginResponse.token,
                            username = loginResponse.username,
                            role = loginResponse.role
                        )
                        println("Login Success: ${UserContext.token}")
                        _loginState.value = LoginState.Success
                        loadProducts()
                    } else {
                        _loginState.value = LoginState.Error("Login Failed: Empty response body")
                    }
                } else {
                    println("Login Failed: ${response.code()}")
                    _loginState.value = LoginState.Error("Login Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
                _loginState.value = LoginState.Error("Error: ${e.message}")
            }
        }
    }

    fun signup(username: String, password: String) {
        viewModelScope.launch {
            _loginState.value = LoginState.Loading
            try {
                val signUpRequest = SignUpRequest(username, password)
                val response = api.register(signUpRequest)

                if (response.isSuccessful) {
                    println("Sign Up Success: ${response.body()}")
                    _loginState.value = LoginState.Success
                } else {
                    println("Sign up Failed: ${response.code()}")
                    _loginState.value = LoginState.Error("Sign up Failed: ${response.code()}")
                }
            } catch (e: Exception) {
                println("Error: ${e.message}")
                _loginState.value = LoginState.Error("Error: ${e.message}")
            }
        }
    }

    fun resetLoginState() {
        _loginState.value = LoginState.Idle
    }

    fun loadProducts() {
        viewModelScope.launch {
            UserContext.token?.let { token ->
                _productState.value = ProductState.Loading
                try {
                    val response = api.getProducts(token)
                    println("token: ${UserContext.token}")
                    if (response.isSuccessful) {
                        val products = response.body() ?: emptyList()
                        _productState.value = ProductState.Success(products)
                    } else {
                        _productState.value =
                            ProductState.Error("Failed to load products: ${response.code()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    _productState.value = ProductState.Error("Error: ${e.message}")
                }
            }
        }
    }

    fun placeOrder(productId: Long, count: Int) {
        viewModelScope.launch {
            UserContext.token?.let { token ->
                _orderState.value = OrderState.Loading
                try {
                    val order = Order(product_id = productId, count = count)
                    val response = api.placeOrder(token, order)
                    if (response.isSuccessful) {
                        _orderState.value = OrderState.Success
                        loadProducts() // Refresh products after order
                    } else {
                        _orderState.value = OrderState.Error("Order failed: ${response.code()}")
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                    println("Error: ${e.message}")
                    _orderState.value = OrderState.Error("Error: ${e.message}")
                }
            }
                ?: run { _orderState.value = OrderState.Error("Not authenticated") }
        }
    }

    fun resetOrderState() {
        _orderState.value = OrderState.Idle
    }

    fun addProduct(name: String, description: String, price: Double, quantity: Int) {
        viewModelScope.launch {
            UserContext.token?.let { token ->
                _adminActionState.value = AdminActionState.Loading
                try {
                    val product =
                        Product(
                            id = 0,
                            name = name,
                            description = description,
                            price = price,
                            quantity = quantity
                        )
                    val response = api.addProduct(token, product)
                    if (response.isSuccessful) {
                        _adminActionState.value = AdminActionState.Success
                        loadProducts()
                    } else {
                        _adminActionState.value =
                            AdminActionState.Error("Failed to add product: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _adminActionState.value = AdminActionState.Error("Error: ${e.message}")
                }
            }
        }
    }

    fun getOllamaProductDescription(productName: String) {
        viewModelScope.launch {
            UserContext.token?.let { token ->
                _ollamaProductDescription.value = OllamaProductDescriptionState.Loading
                try {
                    val response = api.getAiDescription(token, productName)
                    if (response.isSuccessful) {
                        _ollamaProductDescription.value =
                            OllamaProductDescriptionState.Success(
                                response.body() ?: OllamaProductDescriptionResponse(
                                    "No description available"
                                )
                            )
                    } else {
                        _ollamaProductDescription.value =
                            OllamaProductDescriptionState.Error(
                                "Failed to get product description: ${response.code()}"
                            )
                    }
                } catch (e: Exception) {
                    _ollamaProductDescription.value =
                        OllamaProductDescriptionState.Error("Error: ${e.message}")
                }
            }
        }
    }

    fun deleteUser(username: String) {
        viewModelScope.launch {
            UserContext.token?.let { token ->
                _adminActionState.value = AdminActionState.Loading
                try {
                    val response = api.deleteUser(token, username)
                    if (response.isSuccessful) {
                        _adminActionState.value = AdminActionState.Success
                    } else {
                        _adminActionState.value =
                            AdminActionState.Error("Failed to delete user: ${response.code()}")
                    }
                } catch (e: Exception) {
                    _adminActionState.value = AdminActionState.Error("Error: ${e.message}")
                }
            }
        }
    }

    fun resetAdminActionState() {
        _adminActionState.value = AdminActionState.Idle
    }

    fun resetOllamaState() {
        _ollamaProductDescription.value = OllamaProductDescriptionState.Idle
    }
}
