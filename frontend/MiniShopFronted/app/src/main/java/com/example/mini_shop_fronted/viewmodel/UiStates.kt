package com.example.mini_shop_fronted.viewmodel

import com.example.mini_shop_fronted.dto.OllamaProductDescriptionResponse
import com.example.mini_shop_fronted.dto.Product

sealed class LoginState {
    object Idle : LoginState()
    object Loading : LoginState()
    object Success : LoginState()
    data class Error(val message: String) : LoginState()
}

sealed class ProductState {
    object Loading : ProductState()
    data class Success(val products: List<Product>) : ProductState()
    data class Error(val message: String) : ProductState()
}

sealed class OrderState {
    object Idle : OrderState()
    object Loading : OrderState()
    object Success : OrderState()
    data class Error(val message: String) : OrderState()
}

sealed class AdminActionState {
    object Idle : AdminActionState()
    object Loading : AdminActionState()
    object Success : AdminActionState()
    data class Error(val message: String) : AdminActionState()
}

sealed class OllamaProductDescriptionState {
    object Idle : OllamaProductDescriptionState()
    object Loading : OllamaProductDescriptionState()
    data class Success(val response: OllamaProductDescriptionResponse) :
            OllamaProductDescriptionState()

    data class Error(val message: String) : OllamaProductDescriptionState()
}
