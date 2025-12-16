package com.example.mini_shop_fronted

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.example.mini_shop_fronted.ui.components.LoginContent
import com.example.mini_shop_fronted.viewmodel.LoginState
import com.example.mini_shop_fronted.viewmodel.MainViewModel

@Composable
fun LoginScreen(
    viewModel: MainViewModel,
    onNavigateToSignUp: () -> Unit,
    onNavigateToHome: () -> Unit
) {
    var username by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val loginState by viewModel.loginState.collectAsState()

    LaunchedEffect(loginState) {
        if (loginState is LoginState.Success) {
            onNavigateToHome()
            viewModel.resetLoginState()
        }
    }

    LoginContent(
        username = username,
        onUsernameChange = {
            username = it
            viewModel.usernameError = null
        },
        password = password,
        onPasswordChange = {
            password = it
            viewModel.passwordError = null
        },
        onLogin = { viewModel.login(username, password) },
        onNavigateToSignUp = onNavigateToSignUp,
        loginState = loginState,
        usernameError = viewModel.usernameError,
        passwordError = viewModel.passwordError
    )
}
