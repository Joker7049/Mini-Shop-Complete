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
fun SignUpContent(
        username: String,
        onUsernameChange: (String) -> Unit,
        password: String,
        onPasswordChange: (String) -> Unit,
        onSignUp: () -> Unit,
        onNavigateToLogin: () -> Unit,
        signUpState: LoginState
) {
    Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Text(
                text = "Create Account",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Username") },
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
                value = password,
                onValueChange = onPasswordChange,
                label = { Text("Password") },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        if (signUpState is LoginState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                    onClick = onSignUp,
                    modifier = Modifier.fillMaxWidth(),
                    shape = MaterialTheme.shapes.medium
            ) { Text(text = "Sign up", fontSize = 18.sp) }
        }

        if (signUpState is LoginState.Error) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = signUpState.message, color = MaterialTheme.colorScheme.error)
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onNavigateToLogin) { Text(text = "Already have an account? Login") }
    }
}
