package com.example.mini_shop_frontend.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mini_shop_frontend.ui.theme.PrimaryBlue
import com.example.mini_shop_frontend.ui.theme.TextDark
import com.example.mini_shop_frontend.ui.theme.TextGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SignUpScreen(
        onSignUpClick: (String, String, String) -> Unit,
        onLoginClick: () -> Unit,
        onBackClick: () -> Unit
) {
        var username by remember { mutableStateOf("") }
        var email by remember { mutableStateOf("") }
        var password by remember { mutableStateOf("") }
        var isPasswordVisible by remember { mutableStateOf(false) }
        var agreedToTerms by remember { mutableStateOf(false) }

        Surface(color = Color.White, modifier = Modifier.fillMaxSize()) {
                Column(modifier = Modifier.fillMaxSize().padding(horizontal = 24.dp)) {
                        // Back Button
                        IconButton(
                                onClick = onBackClick,
                                modifier = Modifier.padding(top = 16.dp).offset(x = (-12).dp)
                        ) {
                                Icon(
                                        imageVector = Icons.Default.ArrowBack,
                                        contentDescription = "Back",
                                        tint = TextDark
                                )
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                                text = "Let's Get Started",
                                fontSize = 32.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextDark
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                                text = "Create an account to track orders and checkout faster.",
                                fontSize = 16.sp,
                                color = TextGrey
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        // Form Fields
                        Column(verticalArrangement = Arrangement.spacedBy(20.dp)) {
                                // Username
                                AuthField(
                                        label = "Username",
                                        value = username,
                                        onValueChange = { username = it },
                                        placeholder = "Enter your username"
                                )

                                // Email
                                AuthField(
                                        label = "Email Address",
                                        value = email,
                                        onValueChange = { email = it },
                                        placeholder = "Enter your email"
                                )

                                // Password
                                Column {
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
                                                        Text(
                                                                "Create a password",
                                                                color = TextGrey.copy(alpha = 0.6f)
                                                        )
                                                },
                                                modifier = Modifier.fillMaxWidth(),
                                                shape = RoundedCornerShape(12.dp),
                                                visualTransformation =
                                                        if (isPasswordVisible)
                                                                VisualTransformation.None
                                                        else PasswordVisualTransformation(),
                                                trailingIcon = {
                                                        IconButton(
                                                                onClick = {
                                                                        isPasswordVisible =
                                                                                !isPasswordVisible
                                                                }
                                                        ) {
                                                                Icon(
                                                                        imageVector =
                                                                                if (isPasswordVisible
                                                                                )
                                                                                        Icons.Default
                                                                                                .Visibility
                                                                                else
                                                                                        Icons.Default
                                                                                                .VisibilityOff,
                                                                        contentDescription = null,
                                                                        tint = TextGrey
                                                                )
                                                        }
                                                },
                                                colors =
                                                        OutlinedTextFieldDefaults.colors(
                                                                focusedContainerColor = Color.White,
                                                                unfocusedContainerColor =
                                                                        Color.White,
                                                                unfocusedBorderColor =
                                                                        Color(0xFFE2E8F0),
                                                                focusedBorderColor = PrimaryBlue
                                                        ),
                                                singleLine = true,
                                                keyboardOptions =
                                                        KeyboardOptions(
                                                                keyboardType = KeyboardType.Password
                                                        )
                                        )
                                        Text(
                                                text = "Must be at least 8 characters",
                                                fontSize = 12.sp,
                                                color = TextGrey,
                                                modifier = Modifier.padding(top = 8.dp)
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        // Terms Checkbox
                        Row(verticalAlignment = Alignment.CenterVertically) {
                                Checkbox(
                                        checked = agreedToTerms,
                                        onCheckedChange = { agreedToTerms = it },
                                        colors = CheckboxDefaults.colors(checkedColor = PrimaryBlue)
                                )
                                Text(text = "I agree to the ", fontSize = 14.sp, color = TextGrey)
                                Text(
                                        text = "Terms of Service",
                                        fontSize = 14.sp,
                                        color = PrimaryBlue,
                                        fontWeight = FontWeight.SemiBold
                                )
                                Text(text = " and ", fontSize = 14.sp, color = TextGrey)
                                Text(
                                        text = "Privacy Policy",
                                        fontSize = 14.sp,
                                        color = PrimaryBlue,
                                        fontWeight = FontWeight.SemiBold
                                )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                                onClick = { onSignUpClick(username, password, email) },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                                enabled = agreedToTerms
                        ) { Text("Create Account", fontSize = 18.sp, fontWeight = FontWeight.Bold) }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Social Divider
                        Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                        ) {
                                HorizontalDivider(
                                        modifier = Modifier.weight(1f),
                                        color = Color(0xFFE2E8F0)
                                )
                                Text(
                                        text = "Or sign up with",
                                        modifier = Modifier.padding(horizontal = 16.dp),
                                        fontSize = 14.sp,
                                        color = TextGrey
                                )
                                HorizontalDivider(
                                        modifier = Modifier.weight(1f),
                                        color = Color(0xFFE2E8F0)
                                )
                        }

                        Spacer(modifier = Modifier.height(32.dp))

                        // Social Buttons
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(16.dp)
                        ) {
                                SocialButton(
                                        icon = Icons.Default.Add,
                                        label = "Apple",
                                        modifier = Modifier.weight(1f)
                                )
                                SocialButton(
                                        icon = Icons.Default.Check,
                                        label = "Google",
                                        modifier = Modifier.weight(1f)
                                )
                        }

                        Spacer(modifier = Modifier.weight(1f))

                        // Bottom Login Link
                        Row(
                                modifier = Modifier.padding(bottom = 40.dp).fillMaxWidth(),
                                horizontalArrangement = Arrangement.Center,
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Text(
                                        text = "Already have an account? ",
                                        color = TextGrey,
                                        fontSize = 14.sp
                                )
                                TextButton(
                                        onClick = onLoginClick,
                                        contentPadding = PaddingValues(0.dp)
                                ) {
                                        Text(
                                                text = "Log In",
                                                color = PrimaryBlue,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp
                                        )
                                }
                        }
                }
        }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun AuthField(
        label: String,
        value: String,
        onValueChange: (String) -> Unit,
        placeholder: String
) {
        Column {
                Text(
                        text = label,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextDark,
                        modifier = Modifier.padding(bottom = 8.dp)
                )
                OutlinedTextField(
                        value = value,
                        onValueChange = onValueChange,
                        placeholder = { Text(placeholder, color = TextGrey.copy(alpha = 0.6f)) },
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
        }
}

@Composable
private fun SocialButton(
        icon: androidx.compose.ui.graphics.vector.ImageVector,
        label: String,
        modifier: Modifier = Modifier
) {
        OutlinedButton(
                onClick = {},
                modifier = modifier.height(56.dp),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFE2E8F0)),
                colors = ButtonDefaults.outlinedButtonColors(contentColor = TextDark)
        ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(icon, contentDescription = null, modifier = Modifier.size(20.dp))
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = label, fontSize = 16.sp, fontWeight = FontWeight.Medium)
                }
        }
}
