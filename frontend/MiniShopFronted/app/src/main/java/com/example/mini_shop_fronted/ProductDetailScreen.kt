package com.example.mini_shop_fronted

import android.widget.Toast
import androidx.compose.animation.animateContentSize
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mini_shop_fronted.ui.components.ProductImage
import com.example.mini_shop_fronted.ui.components.QuantitySelector
import com.example.mini_shop_fronted.viewmodel.MainViewModel
import com.example.mini_shop_fronted.viewmodel.OrderState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductDetailScreen(
        productId: Long,
        name: String,
        description: String,
        price: Double,
        quantity: Int,
        imageUrl: String?,
        viewModel: MainViewModel,
        onBack: () -> Unit
) {
        val context = LocalContext.current
        val orderState by viewModel.orderState.collectAsState()
        var quantityToBuy by remember { mutableIntStateOf(1) }

        // Handle order state changes
        LaunchedEffect(orderState) {
                when (orderState) {
                        is OrderState.Success -> {
                                Toast.makeText(
                                                context,
                                                "Order placed successfully!",
                                                Toast.LENGTH_SHORT
                                        )
                                        .show()
                                viewModel.resetOrderState()
                        }
                        is OrderState.Error -> {
                                Toast.makeText(
                                                context,
                                                (orderState as OrderState.Error).message,
                                                Toast.LENGTH_LONG
                                        )
                                        .show()
                                viewModel.resetOrderState()
                        }
                        else -> {}
                }
        }

        Scaffold(
                topBar = {
                        TopAppBar(
                                title = { Text("Product Details") },
                                navigationIcon = {
                                        IconButton(onClick = onBack) {
                                                Icon(
                                                        Icons.Default.ArrowBack,
                                                        contentDescription = "Back"
                                                )
                                        }
                                },
                                colors =
                                        TopAppBarDefaults.topAppBarColors(
                                                containerColor =
                                                        MaterialTheme.colorScheme.primaryContainer
                                        )
                        )
                }
        ) { paddingValues ->
                Column(
                        modifier =
                                Modifier.fillMaxSize()
                                        .padding(paddingValues)
                                        .verticalScroll(rememberScrollState())
                                        .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                ) {
                        // Product Icon with gradient background
                        ProductImage(imageUrl)

                        Spacer(modifier = Modifier.height(24.dp))

                        // Product Name
                        Text(
                                text = name,
                                fontSize = 28.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onBackground,
                                textAlign = TextAlign.Center
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Price Tag
                        Card(
                                modifier = Modifier.animateContentSize(),
                                shape = RoundedCornerShape(16.dp),
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor =
                                                        MaterialTheme.colorScheme.secondaryContainer
                                        )
                        ) {
                                Text(
                                        text = "$${"%.2f".format(price)}",
                                        fontSize = 24.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSecondaryContainer,
                                        modifier =
                                                Modifier.padding(
                                                        horizontal = 24.dp,
                                                        vertical = 8.dp
                                                )
                                )
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        // Stock info
                        Text(
                                text = "In Stock: $quantity",
                                fontSize = 16.sp,
                                color =
                                        if (quantity > 0) MaterialTheme.colorScheme.primary
                                        else MaterialTheme.colorScheme.error
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        // Description Card
                        Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(16.dp),
                                colors =
                                        CardDefaults.cardColors(
                                                containerColor =
                                                        MaterialTheme.colorScheme.surfaceVariant
                                        )
                        ) {
                                Column(modifier = Modifier.padding(16.dp)) {
                                        Text(
                                                text = "Description",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                                text = description,
                                                fontSize = 14.sp,
                                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                                lineHeight = 22.sp
                                        )
                                }
                        }

                        Spacer(modifier = Modifier.height(24.dp))

                        QuantitySelector(
                                quantity = quantityToBuy,
                                maxQuantity = quantity,
                                onQuantityChange = { quantityToBuy = it }
                        )

                        Spacer(modifier = Modifier.height(32.dp))

                        Button(
                                onClick = { viewModel.placeOrder(productId, quantityToBuy) },
                                modifier = Modifier.fillMaxWidth().height(56.dp),
                                shape = RoundedCornerShape(16.dp),
                                enabled = quantity > 0 && orderState !is OrderState.Loading,
                                colors =
                                        ButtonDefaults.buttonColors(
                                                containerColor = MaterialTheme.colorScheme.primary
                                        )
                        ) {
                                if (orderState is OrderState.Loading) {
                                        CircularProgressIndicator(
                                                modifier = Modifier.size(24.dp),
                                                color = Color.White
                                        )
                                } else {
                                        Text(
                                                text = "Buy Now",
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold
                                        )
                                }
                        }
                }
        }
}
