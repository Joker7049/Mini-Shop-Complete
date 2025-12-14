package com.example.mini_shop_fronted

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mini_shop_fronted.dto.Product
import com.example.mini_shop_fronted.ui.components.ProductItem
import com.example.mini_shop_fronted.utils.UserContext
import com.example.mini_shop_fronted.viewmodel.MainViewModel
import com.example.mini_shop_fronted.viewmodel.ProductState

@Composable
fun HomeScreen(
        viewModel: MainViewModel,
        onLogout: () -> Unit,
        onProductClick: (Product) -> Unit,
        onNavigateToAdmin: () -> Unit
) {
        val productState by viewModel.productState.collectAsState()
        val isAdmin by UserContext.isAdmin.collectAsState()

        Column(
                modifier = Modifier.fillMaxSize().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                ) {
                        Text(
                                text = "Mini Shop",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                        )
                        IconButton(onClick = onLogout) {
                                Icon(
                                        imageVector = Icons.Default.ExitToApp,
                                        contentDescription = "Logout",
                                        tint = MaterialTheme.colorScheme.error
                                )
                        }
                }

                if (isAdmin) {
                        Button(
                                onClick = onNavigateToAdmin,
                                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                                colors =
                                        ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.tertiary
                                        )
                        ) { Text("Go to Admin Panel") }
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (val state = productState) {
                        is ProductState.Loading -> {
                                Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                ) { CircularProgressIndicator() }
                        }
                        is ProductState.Success -> {
                                LazyVerticalGrid(
                                        columns = GridCells.Fixed(2),
                                        contentPadding = PaddingValues(8.dp),
                                        verticalArrangement = Arrangement.spacedBy(16.dp),
                                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                                ) {
                                        items(state.products) { product ->
                                                ProductItem(
                                                        product = product,
                                                        onClick = { onProductClick(product) }
                                                )
                                        }
                                }
                        }
                        is ProductState.Error -> {
                                Box(
                                        modifier = Modifier.fillMaxSize(),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text(
                                                text = state.message,
                                                color = MaterialTheme.colorScheme.error
                                        )
                                }
                        }
                }
        }
}
