package com.example.mini_shop_fronted

import androidx.compose.runtime.*
import com.example.mini_shop_fronted.ui.components.SignUpContent
import com.example.mini_shop_fronted.viewmodel.LoginState
import com.example.mini_shop_fronted.viewmodel.MainViewModel

@Composable
fun SignUpScreen(
        onNavigateToLogin: () -> Unit,
        onNavigateToHome: () -> Unit,
        viewModel: MainViewModel
) {
        var username by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        val signUpState by viewModel.loginState.collectAsState()

        LaunchedEffect(signUpState) {
                if (signUpState is LoginState.Success) {
                        onNavigateToHome()
                        viewModel.resetLoginState()
                }
        }

        SignUpContent(
                username = username,
                onUsernameChange = { username = it },
                password = password,
                onPasswordChange = { password = it },
                onSignUp = { viewModel.signup(username, password) },
                onNavigateToLogin = onNavigateToLogin,
                signUpState = signUpState
        )
}
