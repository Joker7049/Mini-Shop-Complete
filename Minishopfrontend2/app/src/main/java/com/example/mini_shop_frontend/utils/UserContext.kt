package com.example.mini_shop_frontend.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserContext {
    private var _token: String? = null
    val token: String?
        get() = _token

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    fun setUserData(token: String, username: String, role: String) {
        _token = "Bearer $token"
        _isAdmin.value = role == "ROLE_ADMIN"
    }

    fun clear() {
        _token = null
        _isAdmin.value = false
    }
}
