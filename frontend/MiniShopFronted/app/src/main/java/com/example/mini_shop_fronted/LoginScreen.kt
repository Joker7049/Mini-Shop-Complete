package com.example.mini_shop_fronted

import androidx.compose.runtime.*
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
            onUsernameChange = { username = it },
            password = password,
            onPasswordChange = { password = it },
            onLogin = { viewModel.login(username, password) },
            onNavigateToSignUp = onNavigateToSignUp,
            loginState = loginState
    )
}
