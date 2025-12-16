package com.example.mini_shop_fronted.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mini_shop_fronted.viewmodel.LoginState

@Composable
fun LoginContent(
        username: String,
        onUsernameChange: (String) -> Unit,
        password: String,
        onPasswordChange: (String) -> Unit,
        onLogin: () -> Unit,
        onNavigateToSignUp: () -> Unit,
        loginState: LoginState,
        usernameError: String?,
        passwordError: String?
) {
    Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Text(
                text = "Welcome Back",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Username") },
                supportingText = {
                    if (usernameError != null) {
                        Text(
                                text = usernameError,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                supportingText = {
                    if (passwordError != null) {
                        Text(
                                text = passwordError,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                        )
                    }
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (loginState is LoginState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                    onClick = onLogin,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
            ) { Text(text = "Login", fontSize = 18.sp) }
        }

        if (loginState is LoginState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = loginState.message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToSignUp) { Text(text = "Don't have an account? Sign Up") }
    }
}
