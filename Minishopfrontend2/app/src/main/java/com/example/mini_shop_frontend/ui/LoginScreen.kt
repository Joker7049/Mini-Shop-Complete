package com.example.mini_shop_frontend.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mini_shop_frontend.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
        onLoginClick: (String, String) -> Unit,
        onSignUpClick: () -> Unit,
        onForgotPasswordClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var isPasswordVisible by remember { mutableStateOf(false) }

    Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
        Column(
                modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.padding(top = 80.dp)
            ) {
                // Logo Icon
                Surface(
                        modifier =
                                Modifier.size(80.dp)
                                        .shadow(
                                                10.dp,
                                                RoundedCornerShape(20.dp),
                                                spotColor = PrimaryBlue.copy(alpha = 0.5f)
                                        ),
                        shape = RoundedCornerShape(20.dp),
                        color = PrimaryBlue
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                                imageVector = Icons.Default.ShoppingBag,
                                contentDescription = "Logo",
                                tint = Color.White,
                                modifier = Modifier.size(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                        text = "Welcome Back",
                        fontSize = 32.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                        text = "Please sign in to continue shopping your favorite items.",
                        fontSize = 16.sp,
                        color = TextGrey,
                        modifier = Modifier.padding(horizontal = 40.dp),
                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                )
            }

            Column(modifier = Modifier.fillMaxWidth()) {
                // Username/Email Field
                Text(
                        text = "Username or Email",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark,
                        modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = {
                            Text(
                                    "Enter your username or email",
                                    color = TextGrey.copy(alpha = 0.6f)
                            )
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        unfocusedBorderColor = Color(0xFFE2E8F0),
                                        focusedBorderColor = PrimaryBlue
                                ),
                        singleLine = true
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Password Field
                Text(
                        text = "Password",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark,
                        modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = {
                            Text("Enter your password", color = TextGrey.copy(alpha = 0.6f))
                        },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        visualTransformation =
                                if (isPasswordVisible) VisualTransformation.None
                                else PasswordVisualTransformation(),
                        trailingIcon = {
                            IconButton(onClick = { isPasswordVisible = !isPasswordVisible }) {
                                Icon(
                                        imageVector =
                                                if (isPasswordVisible) Icons.Default.Visibility
                                                else Icons.Default.VisibilityOff,
                                        contentDescription =
                                                if (isPasswordVisible) "Hide password"
                                                else "Show password",
                                        tint = TextGrey
                                )
                            }
                        },
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        focusedContainerColor = Color.White,
                                        unfocusedContainerColor = Color.White,
                                        unfocusedBorderColor = Color(0xFFE2E8F0),
                                        focusedBorderColor = PrimaryBlue
                                ),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password)
                )

                TextButton(
                        onClick = onForgotPasswordClick,
                        modifier = Modifier.align(Alignment.End)
                ) {
                    Text(
                            text = "Forgot Password?",
                            color = PrimaryBlue,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                        onClick = { onLoginClick(email, password) },
                        modifier =
                                Modifier.fillMaxWidth()
                                        .height(56.dp)
                                        .shadow(
                                                4.dp,
                                                RoundedCornerShape(12.dp),
                                                spotColor = PrimaryBlue
                                        ),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                ) { Text("Log In", fontSize = 18.sp, fontWeight = FontWeight.Bold) }
            }

            Row(
                    modifier = Modifier.padding(bottom = 40.dp).fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "Don't have an account? ", color = TextGrey, fontSize = 14.sp)
                TextButton(onClick = onSignUpClick, contentPadding = PaddingValues(0.dp)) {
                    Text(
                            text = "Sign up",
                            color = PrimaryBlue,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp
                    )
                }
            }
        }
    }
}
