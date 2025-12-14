package com.example.mini_shop_fronted.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mini_shop_fronted.dto.Product

@Composable
fun ProductItem(product: Product, onClick: () -> Unit) {
    Card(
            modifier = Modifier.fillMaxWidth().height(200.dp).clickable(onClick = onClick),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
            colors =
                    CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
    ) {
        Column(
                modifier = Modifier.fillMaxSize().padding(12.dp),
                verticalArrangement = Arrangement.SpaceBetween,
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                    imageVector = Icons.Default.ShoppingCart,
                    contentDescription = "Product Icon",
                    modifier = Modifier.size(64.dp),
                    tint = MaterialTheme.colorScheme.primary
            )

            Text(
                    text = product.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
            )

            Text(
                    text = "$${product.price}",
                    fontSize = 14.sp,
                    color = MaterialTheme.colorScheme.secondary
            )

            Text(text = "Qty: ${product.quantity}", fontSize = 12.sp, color = Color.Gray)
        }
    }
}
