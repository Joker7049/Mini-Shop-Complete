package com.example.mini_shop_fronted.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ProductImage() {
    Box(
            modifier =
                    Modifier.size(180.dp)
                            .clip(CircleShape)
                            .background(
                                    Brush.linearGradient(
                                            colors =
                                                    listOf(
                                                            MaterialTheme.colorScheme.primary,
                                                            MaterialTheme.colorScheme.tertiary
                                                    )
                                    )
                            ),
            contentAlignment = Alignment.Center
    ) {
        Icon(
                imageVector = Icons.Default.ShoppingCart,
                contentDescription = "Product",
                modifier = Modifier.size(100.dp),
                tint = Color.White
        )
    }
}

@Composable
fun QuantitySelector(quantity: Int, maxQuantity: Int, onQuantityChange: (Int) -> Unit) {
    Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
    ) {
        Text("Quantity: ", fontSize = 16.sp)
        OutlinedButton(
                onClick = { if (quantity > 1) onQuantityChange(quantity - 1) },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
        ) { Text("-", fontSize = 20.sp) }
        Text(
                text = "$quantity",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 16.dp)
        )
        OutlinedButton(
                onClick = { if (quantity < maxQuantity) onQuantityChange(quantity + 1) },
                modifier = Modifier.size(40.dp),
                contentPadding = PaddingValues(0.dp)
        ) { Text("+", fontSize = 20.sp) }
    }
}
