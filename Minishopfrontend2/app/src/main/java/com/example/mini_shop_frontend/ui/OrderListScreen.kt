package com.example.mini_shop_frontend.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mini_shop_frontend.model.OrderHistoryDto
import com.example.mini_shop_frontend.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OrderListScreen(
        orders: List<OrderHistoryDto>,
        onBackClick: () -> Unit,
        onOrderClick: (Long) -> Unit = {}
) {
    var selectedFilter by remember { mutableStateOf("All") }
    val filters = listOf("All", "Delivered", "Pending", "Cancelled")

    val filteredOrders =
            if (selectedFilter == "All") {
                orders
            } else {
                orders.filter { it.status.contains(selectedFilter, ignoreCase = true) }
            }

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Text(
                                    "My Orders",
                                    modifier = Modifier.fillMaxWidth(),
                                    textAlign = TextAlign.Center,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                            )
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        actions = {
                            Spacer(modifier = Modifier.width(48.dp)) // To balance the back button
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                        windowInsets = WindowInsets(0.dp)
                )
            },
            containerColor = Color(0xFFF9FAFB),
            contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        Column(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Filter Chips
            LazyRow(
                    modifier =
                            Modifier.fillMaxWidth()
                                    .background(Color.White)
                                    .padding(vertical = 12.dp),
                    contentPadding = PaddingValues(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    FilterChip(
                            selected = selectedFilter == filter,
                            onClick = { selectedFilter = filter },
                            label = { Text(filter) },
                            shape = RoundedCornerShape(20.dp),
                            colors =
                                    FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = PrimaryBlue,
                                            selectedLabelColor = Color.White,
                                            containerColor = Color.White,
                                            labelColor = TextGrey
                                    ),
                            border =
                                    FilterChipDefaults.filterChipBorder(
                                            borderColor =
                                                    if (selectedFilter == filter) PrimaryBlue
                                                    else Color(0xFFE5E7EB),
                                            borderWidth = 1.dp,
                                            enabled = true,
                                            selected = selectedFilter == filter
                                    )
                    )
                }
            }

            // Orders List
            if (filteredOrders.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text("No orders found", color = TextGrey)
                }
            } else {
                LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                ) { items(filteredOrders) { order -> OrderCard(order, onOrderClick) } }
            }
        }
    }
}

@Composable
private fun OrderCard(order: OrderHistoryDto, onClick: (Long) -> Unit) {
    Card(
            modifier = Modifier.fillMaxWidth().clickable { onClick(order.id) },
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Top Header: Date and Order ID
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "${order.orderDate} • #${order.id}", fontSize = 12.sp, color = TextGrey)
                StatusBadge(status = order.status)
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Middle Section: Image and Info
            Row(modifier = Modifier.fillMaxWidth()) {
                AsyncImage(
                        model = order.productImageUrl,
                        contentDescription = null,
                        modifier =
                                Modifier.size(80.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(Color(0xFFF3F4F6)),
                        contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                            text = order.productName,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = TextDark
                    )
                    Text(text = "Qty: ${order.quantity}", fontSize = 14.sp, color = TextGrey)
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
            HorizontalDivider(color = Color(0xFFF3F4F6))
            Spacer(modifier = Modifier.height(16.dp))

            // Bottom Section: Price and Actions
            Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                        text = "$${String.format("%.2f", order.totalPrice)}",
                        fontWeight = FontWeight.Black,
                        fontSize = 18.sp,
                        color = TextDark
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    when (order.status.lowercase()) {
                        "delivered" -> {
                            OrderActionButton(text = "Details", onClick = {})
                            OrderActionButton(text = "Buy Again", primary = true, onClick = {})
                        }
                        "pending" -> {
                            OrderActionButton(text = "Track Order", primary = true, onClick = {})
                        }
                        "cancelled" -> {
                            OrderActionButton(text = "View Details", onClick = {})
                        }
                        else -> {
                            OrderActionButton(text = "Details", onClick = {})
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: String) {
    val (color, icon) =
            when (status.lowercase()) {
                "delivered" -> Color(0xFF10B981) to Icons.Default.CheckCircle
                "pending" -> Color(0xFFF59E0B) to Icons.Default.Schedule
                "cancelled" -> Color(0xFFEF4444) to Icons.Default.Cancel
                else -> TextGrey to Icons.Default.Schedule
            }

    Surface(color = color.copy(alpha = 0.1f), shape = RoundedCornerShape(4.dp)) {
        Row(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(icon, contentDescription = null, modifier = Modifier.size(14.dp), tint = color)
            Text(status, color = color, fontSize = 12.sp, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun OrderActionButton(text: String, primary: Boolean = false, onClick: () -> Unit) {
    Button(
            onClick = onClick,
            modifier = Modifier.height(36.dp),
            shape = RoundedCornerShape(8.dp),
            colors =
                    ButtonDefaults.buttonColors(
                            containerColor = if (primary) PrimaryBlue else Color(0xFFEFF6FF),
                            contentColor = if (primary) Color.White else PrimaryBlue
                    ),
            contentPadding = PaddingValues(horizontal = 16.dp)
    ) { Text(text = text, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
}
