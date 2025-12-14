package com.example.mini_shop_fronted.utils

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

object UserContext {
    private var _token: String? = null
    val token: String?
        get() = _token

    private var _username: String? = null
    val username: String?
        get() = _username

    private var _role: String? = null
    val role: String?
        get() = _role

    private val _isAdmin = MutableStateFlow(false)
    val isAdmin: StateFlow<Boolean> = _isAdmin.asStateFlow()

    fun setUserData(token: String, username: String, role: String) {
        _token = "Bearer $token"
        _username = username
        _role = role
        _isAdmin.value = role == "ADMIN"
    }

    fun clear() {
        _token = null
        _username = null
        _role = null
        _isAdmin.value = false
    }
}
