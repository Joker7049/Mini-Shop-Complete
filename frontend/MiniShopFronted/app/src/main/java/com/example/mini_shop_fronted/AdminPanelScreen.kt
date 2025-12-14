package com.example.mini_shop_fronted

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mini_shop_fronted.viewmodel.MainViewModel

@Composable
fun AdminPanelScreen(
        viewModel: MainViewModel,
        onNavigateHome: () -> Unit,
        onNavigateToAddProduct: () -> Unit,
        onNavigateToDeleteUser: () -> Unit
) {
        Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
        ) {
                Text(
                        text = "Admin Panel",
                        fontSize = 24.sp,
                        style = MaterialTheme.typography.headlineMedium
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                        onClick = onNavigateToAddProduct,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) { Text("Add Product") }

                Button(
                        onClick = onNavigateToDeleteUser,
                        modifier = Modifier.fillMaxWidth().padding(8.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = MaterialTheme.colorScheme.error
                                )
                ) { Text("Delete User") }

                Spacer(modifier = Modifier.height(16.dp))

                OutlinedButton(
                        onClick = onNavigateHome,
                        modifier = Modifier.fillMaxWidth().padding(8.dp)
                ) { Text("Back to Home") }
        }
}
