package com.example.mini_shop_frontend.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mini_shop_frontend.model.CartDto
import com.example.mini_shop_frontend.network.RetrofitInstance
import com.example.mini_shop_frontend.utils.UserContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

class CartViewModel : ViewModel() {

    private val _cart = MutableStateFlow<CartDto?>(null)
    val cart: StateFlow<CartDto?> = _cart.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _snackMessage = MutableStateFlow<String?>(null)
    val snackMessage: StateFlow<String?> = _snackMessage.asStateFlow()
    fun fetchCart() {
        executeApiCall {
            val token = UserContext.token ?: throw Exception("Not logged in")
            RetrofitInstance.api.getCart(token)
        }
    }

    fun addToCart(productId: Long, quantity: Int = 1) {
        executeApiCall {
            val token = UserContext.token ?: throw Exception("Not logged in")
            val response = RetrofitInstance.api.addToCart(token, productId, quantity)
            if (response.isSuccessful) {
                showSnack("Added to cart!")
            }
            response
        }
    }

    fun updateQuantity(productId: Long, delta: Int) {
        executeApiCall {
            val token = UserContext.token ?: throw Exception("Not logged in")
            // Reusing addToCart because the backend increments the quantity by the value passed
            RetrofitInstance.api.addToCart(token, productId, delta)
        }
    }

    fun removeFromCart(productId: Long) {
        executeApiCall {
            val token = UserContext.token ?: throw Exception("Not logged in")
            RetrofitInstance.api.removeFromCart(token, productId)
        }
    }

    fun clearCart() {
        executeApiCall {
            val token = UserContext.token ?: throw Exception("Not logged in")
            RetrofitInstance.api.clearCart(token)
        }
    }

    fun checkout(onSuccess: () -> Unit) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val token = UserContext.token ?: throw Exception("Not logged in")
                val response = RetrofitInstance.api.checkout(token)
                if (response.isSuccessful) {
                    showSnack("Checkout successful! Order placed.")
                    _cart.value = null // Empty locally
                    onSuccess()
                    fetchCart() // Refresh just in case
                } else {
                    _error.value = "Checkout failed: ${response.code()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun showSnack(message: String?) {
        _snackMessage.value = message
    }

    /**
     * Helper function to reduce boilerplate for API calls. This is a "Senior Dev" pattern to keep
     * the code DRY and consistent.
     */
    private fun executeApiCall(call: suspend () -> Response<CartDto>) {
        viewModelScope.launch {
            try {
                _isLoading.value = true
                _error.value = null
                val response = call()
                if (response.isSuccessful) {
                    _cart.value = response.body()
                } else {
                    _error.value = "Error: ${response.code()} - ${response.message()}"
                }
            } catch (e: Exception) {
                _error.value = e.message ?: "Unknown error occurred"
            } finally {
                _isLoading.value = false
            }
        }
    }
}
