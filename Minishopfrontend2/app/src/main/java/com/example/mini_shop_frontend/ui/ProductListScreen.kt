package com.example.mini_shop_frontend.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mini_shop_frontend.Product
import com.example.mini_shop_frontend.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
        title: String,
        products: List<Product>,
        onBackClick: () -> Unit = {},
        onCartClick: () -> Unit = {},
        onProductClick: (Long) -> Unit = {}
) {
        var selectedCategory by remember { mutableStateOf("All") }

        Scaffold(
                topBar = {
                        TopAppBar(
                                title = {
                                        Text(
                                                title,
                                                fontSize = 18.sp,
                                                fontWeight = FontWeight.Bold,
                                                modifier = Modifier.fillMaxWidth(),
                                                textAlign = TextAlign.Center
                                        )
                                },
                                navigationIcon = {
                                        IconButton(onClick = onBackClick) {
                                                Icon(
                                                        Icons.Default.ArrowBack,
                                                        contentDescription = "Back"
                                                )
                                        }
                                },
                                actions = {
                                        IconButton(onClick = onCartClick) {
                                                Icon(
                                                        Icons.Outlined.ShoppingCart,
                                                        contentDescription = "Cart"
                                                )
                                        }
                                },
                                colors =
                                        TopAppBarDefaults.topAppBarColors(
                                                containerColor = Color.White
                                        ),
                                windowInsets = WindowInsets(0.dp)
                        )
                },
                containerColor = Color(0xFFF9FAFB),
                contentWindowInsets = WindowInsets(0.dp)
        ) { paddingValues ->
                LazyColumn(
                        modifier = Modifier.fillMaxSize().padding(paddingValues),
                        contentPadding = PaddingValues(bottom = 16.dp)
                ) {
                        // Filter & Sort Bar
                        item { FilterSortBar() }

                        // Categories
                        item {
                                CategoryChips(
                                        selectedCategory = selectedCategory,
                                        onCategorySelected = { selectedCategory = it }
                                )
                        }

                        // Product List
                        items(products) { product ->
                                ProductListCard(
                                        product = product,
                                        onClick = { onProductClick(product.id) }
                                )
                        }

                        // Skeleton Loader (Example/Placeholder)
                        item { SkeletonProductCard() }
                }
        }
}

@Composable
fun FilterSortBar() {
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
                // Filter Button
                Surface(
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, Color(0xFFE5E7EB)),
                        color = Color.White
                ) {
                        Row(
                                modifier = Modifier.padding(horizontal = 12.dp),
                                verticalAlignment = Alignment.CenterVertically
                        ) {
                                Icon(
                                        imageVector = Icons.Default.Tune,
                                        contentDescription = null,
                                        modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Filter", fontSize = 14.sp, fontWeight = FontWeight.Medium)
                                Spacer(modifier = Modifier.width(6.dp))
                                // Badge
                                Box(
                                        modifier =
                                                Modifier.size(18.dp)
                                                        .clip(CircleShape)
                                                        .background(PrimaryBlue),
                                        contentAlignment = Alignment.Center
                                ) {
                                        Text(
                                                "2",
                                                color = Color.White,
                                                fontSize = 10.sp,
                                                fontWeight = FontWeight.Bold
                                        )
                                }
                        }
                }

                // Sort Dropdown Placeholder
                Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(horizontalAlignment = Alignment.End) {
                                Text("Sort:", fontSize = 12.sp, color = TextGrey)
                                Text(
                                        "Recommended",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                )
                        }
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                                Icons.Default.KeyboardArrowDown,
                                contentDescription = null,
                                tint = TextGrey
                        )
                }

                // List/Grid Toggle
                IconButton(onClick = {}) {
                        Icon(Icons.Default.List, contentDescription = "Toggle View")
                }
        }
}

@Composable
fun CategoryChips(selectedCategory: String, onCategorySelected: (String) -> Unit) {
        val categories = listOf("All", "Running", "Lifestyle", "Jordan", "Training")

        LazyRow(
                modifier = Modifier.fillMaxWidth().padding(vertical = 12.dp),
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
                items(categories) { category ->
                        val isSelected = category == selectedCategory
                        Surface(
                                modifier = Modifier.clickable { onCategorySelected(category) },
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) TextDark else Color.White,
                                border =
                                        if (isSelected) null
                                        else BorderStroke(1.dp, Color(0xFFE5E7EB))
                        ) {
                                Text(
                                        text = category,
                                        modifier =
                                                Modifier.padding(
                                                        horizontal = 20.dp,
                                                        vertical = 8.dp
                                                ),
                                        color = if (isSelected) Color.White else TextGrey,
                                        fontSize = 14.sp,
                                        fontWeight =
                                                if (isSelected) FontWeight.Bold
                                                else FontWeight.Medium
                                )
                        }
                }
        }
}

@Composable
fun ProductListCard(product: Product, onClick: () -> Unit) {
        Card(
                modifier =
                        Modifier.fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 8.dp)
                                .clickable { onClick() },
                shape = RoundedCornerShape(24.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
        ) {
                Column {
                        // Image Stack
                        Box(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .height(280.dp)
                                                .background(Color(0xFFF3F4F6)) // Base background
                        ) {
                                AsyncImage(
                                        model = product.imageUrl,
                                        contentDescription = product.name,
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Fit
                                )

                                // Wishlist Icon
                                Surface(
                                        modifier =
                                                Modifier.padding(16.dp)
                                                        .size(40.dp)
                                                        .align(Alignment.TopEnd),
                                        shape = CircleShape,
                                        color = Color.White.copy(alpha = 0.9f)
                                ) {
                                        Box(contentAlignment = Alignment.Center) {
                                                Icon(
                                                        imageVector = Icons.Default.FavoriteBorder,
                                                        contentDescription = "Wishlist",
                                                        tint = TextDark,
                                                        modifier = Modifier.size(20.dp)
                                                )
                                        }
                                }

                                // Tag (e.g., NEW ARRIVAL)
                                if (product.discountTag != null || product.isBestSeller) {
                                        val tagText = product.discountTag ?: "BEST SELLER"
                                        Surface(
                                                modifier =
                                                        Modifier.padding(16.dp)
                                                                .align(Alignment.TopStart),
                                                shape = RoundedCornerShape(8.dp),
                                                color =
                                                        if (product.discountTag != null)
                                                                Color(0xFFEF4444)
                                                        else Color.White
                                        ) {
                                                Text(
                                                        text = tagText,
                                                        modifier =
                                                                Modifier.padding(
                                                                        horizontal = 8.dp,
                                                                        vertical = 4.dp
                                                                ),
                                                        color =
                                                                if (product.discountTag != null)
                                                                        Color.White
                                                                else TextDark,
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Black
                                                )
                                        }
                                }
                        }

                        // Product Details
                        Column(modifier = Modifier.padding(20.dp)) {
                                Text(
                                        "NEW ARRIVAL", // Secondary Tag or Category
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextGrey
                                )
                                Text(
                                        text = product.name,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextDark
                                )
                                Text(
                                        text = product.description?.take(40)
                                                        ?: "Sustainable Materials",
                                        fontSize = 12.sp,
                                        color = TextGrey,
                                        maxLines = 1
                                )

                                Spacer(modifier = Modifier.height(16.dp))

                                Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                ) {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                                Text(
                                                        text = "$${product.price}",
                                                        fontSize = 20.sp,
                                                        fontWeight = FontWeight.Black,
                                                        color = TextDark
                                                )
                                                if (product.oldPrice != null) {
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(
                                                                text = "$${product.oldPrice}",
                                                                fontSize = 14.sp,
                                                                color = TextGrey,
                                                                textDecoration =
                                                                        TextDecoration.LineThrough
                                                        )
                                                }
                                        }

                                        Button(
                                                onClick = { /* Add to cart */},
                                                shape = RoundedCornerShape(12.dp),
                                                colors =
                                                        if (product.isBestSeller)
                                                                ButtonDefaults.buttonColors(
                                                                        containerColor = PrimaryBlue
                                                                )
                                                        else
                                                                ButtonDefaults.buttonColors(
                                                                        containerColor =
                                                                                Color(0xFFEFF6FF),
                                                                        contentColor = PrimaryBlue
                                                                ),
                                                contentPadding =
                                                        PaddingValues(
                                                                horizontal = 20.dp,
                                                                vertical = 10.dp
                                                        )
                                        ) {
                                                Text(
                                                        "Add to Cart",
                                                        fontWeight = FontWeight.Bold,
                                                        fontSize = 14.sp
                                                )
                                        }
                                }
                        }
                }
        }
}

@Composable
fun SkeletonProductCard() {
        Card(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                shape = RoundedCornerShape(24.dp),
                colors =
                        CardDefaults.cardColors(
                                containerColor = Color(0xFFF3F4F6).copy(alpha = 0.5f)
                        )
        ) {
                Column(modifier = Modifier.padding(20.dp)) {
                        Box(
                                modifier =
                                        Modifier.fillMaxWidth()
                                                .height(20.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color.LightGray.copy(alpha = 0.3f))
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                                modifier =
                                        Modifier.fillMaxWidth(0.7f)
                                                .height(20.dp)
                                                .clip(RoundedCornerShape(4.dp))
                                                .background(Color.LightGray.copy(alpha = 0.3f))
                        )
                        Spacer(modifier = Modifier.height(24.dp))
                        Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                                Box(
                                        modifier =
                                                Modifier.width(80.dp)
                                                        .height(30.dp)
                                                        .clip(RoundedCornerShape(8.dp))
                                                        .background(
                                                                Color.LightGray.copy(alpha = 0.3f)
                                                        )
                                )
                                Box(
                                        modifier =
                                                Modifier.width(100.dp)
                                                        .height(40.dp)
                                                        .clip(RoundedCornerShape(12.dp))
                                                        .background(
                                                                Color.LightGray.copy(alpha = 0.3f)
                                                        )
                                )
                        }
                }
        }
}

@Composable
private fun Icon(
        imageVector: androidx.compose.ui.graphics.vector.ImageVector,
        contentDescription: String?,
        modifier: Modifier = Modifier,
        tint: Color = androidx.compose.material3.LocalContentColor.current
) {
        androidx.compose.material3.Icon(imageVector, contentDescription, modifier, tint)
}
