package com.example.mini_shop_frontend.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.example.mini_shop_frontend.ui.theme.PrimaryBlue
import com.example.mini_shop_frontend.ui.theme.TextDark
import com.example.mini_shop_frontend.ui.theme.TextGrey

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductListScreen(
        title: String,
        products: List<Product>,
        onBackClick: () -> Unit = {},
        onCartClick: () -> Unit = {},
        onProductClick: (Long) -> Unit = {},
        onSearch: (String) -> Unit,
        onFilter: (Double) -> Unit
) {
        var selectedCategory by remember { mutableStateOf("All") }
        var searchQuery by remember { mutableStateOf("") }
        var showFilterDialog by remember { mutableStateOf(false) }

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
                Box(modifier = Modifier.padding(paddingValues)) {
                        LazyColumn(
                                modifier = Modifier.fillMaxSize(),
                                contentPadding = PaddingValues(bottom = 16.dp)
                        ) {
                                // Filter & Sort Bar
                                item {
                                        FilterSortBar(
                                                searchQuery = searchQuery,
                                                onSearchQueryChanged = {
                                                        searchQuery = it
                                                        onSearch(it)
                                                },
                                                onFilterClick = { showFilterDialog = true }
                                        )
                                }

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

                        if (showFilterDialog) {
                                FilterDialog(
                                        onDismiss = { showFilterDialog = false },
                                        onApplyFilter = { price ->
                                                showFilterDialog = false
                                                onFilter(price)
                                        }
                                )
                        }
                }
        }
}

@Composable
fun FilterSortBar(
        searchQuery: String,
        onSearchQueryChanged: (String) -> Unit,
        onFilterClick: () -> Unit
) {
        Row(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
                Surface(
                        modifier = Modifier.height(36.dp).clickable { onFilterClick() },
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
                        }
                }

                Spacer(modifier = Modifier.width(8.dp))
                OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChanged,
                        modifier =
                                Modifier.weight(1f) // Take up available space
                                        .padding(end = 8.dp),
                        placeholder = { Text("Search products...") },
                        singleLine = true,
                        trailingIcon = {
                                Icon(Icons.Default.Search, contentDescription = "Search")
                        },
                        shape = RoundedCornerShape(12.dp),
                        colors =
                                OutlinedTextFieldDefaults.colors(
                                        unfocusedBorderColor = Color(0xFFE5E7EB),
                                        focusedBorderColor = PrimaryBlue
                                )
                )
        }
}

@Composable
fun FilterDialog(onDismiss: () -> Unit, onApplyFilter: (Double) -> Unit) {
        var priceLimit by remember {
                mutableStateOf(100f)
        } // Default slider value (float for Slider)

        androidx.compose.material3.AlertDialog(
                onDismissRequest = onDismiss,
                title = { Text(text = "Filter by Price", fontWeight = FontWeight.Bold) },
                text = {
                        Column {
                                Text(text = "Max Price: $${priceLimit.toInt()}")
                                androidx.compose.material3.Slider(
                                        value = priceLimit,
                                        onValueChange = { priceLimit = it },
                                        valueRange =
                                                0f..1000f, // Adjust range based on your product
                                        // data
                                        steps = 19 // Optional: steps for the slider
                                )
                        }
                },
                confirmButton = {
                        Button(
                                onClick = {
                                        onApplyFilter(priceLimit.toDouble())
                                        onDismiss()
                                },
                                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                        ) { Text("Apply") }
                },
                dismissButton = {
                        androidx.compose.material3.TextButton(onClick = onDismiss) {
                                Text("Cancel", color = TextGrey)
                        }
                },
                containerColor = Color.White,
                shape = RoundedCornerShape(16.dp)
        )
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
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                        Text(
                                                text = product.description?.take(40)
                                                                ?: "Sustainable Materials",
                                                fontSize = 12.sp,
                                                color = TextGrey,
                                                maxLines = 1,
                                                modifier = Modifier.weight(1f)
                                        )
                                        Surface(
                                                color =
                                                        if (product.quantity > 0) Color(0xFFF3F4F6)
                                                        else Color(0xFFFFEBEE),
                                                shape = RoundedCornerShape(4.dp)
                                        ) {
                                                Text(
                                                        text =
                                                                if (product.quantity > 0)
                                                                        "${product.quantity} avail."
                                                                else "Out of stock",
                                                        color =
                                                                if (product.quantity > 0) TextGrey
                                                                else Color(0xFFC62828),
                                                        fontSize = 10.sp,
                                                        fontWeight = FontWeight.Bold,
                                                        modifier =
                                                                Modifier.padding(
                                                                        horizontal = 4.dp,
                                                                        vertical = 2.dp
                                                                )
                                                )
                                        }
                                }

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
