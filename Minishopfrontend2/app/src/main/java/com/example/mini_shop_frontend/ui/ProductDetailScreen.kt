package com.example.mini_shop_frontend.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBackIosNew
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.ArrowForwardIos
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.mini_shop_frontend.Product
import com.example.mini_shop_frontend.ui.theme.Amber500
import com.example.mini_shop_frontend.ui.theme.BackgroundLight
import com.example.mini_shop_frontend.ui.theme.PrimaryBlue
import com.example.mini_shop_frontend.ui.theme.TextDark
import com.example.mini_shop_frontend.ui.theme.TextGrey

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductDetailScreen(
        product: Product,
        onBackClick: () -> Unit = {},
        onAddToCartClick: () -> Unit = {},
) {
    Scaffold(
            bottomBar = { StickyAddtoCartBar(product, onAddToCartClick) },
            containerColor = Color.White,
            contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        LazyColumn(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
            // Top Navigation
            item { DetailTopAppBar(onBackClick) }

            // Image Gallery
            item { ProductImageGallery(listOf(product.imageUrl ?: "")) }

            // Product Info Header
            item { ProductHeader(product) }

            // Color Selection
            item { ColorSelectionSection() }

            // Size Selection
            item { SizeSelectionSection() }

            // Description
            item { DescriptionSection(product.description ?: "No description available.") }

            // Reviews
            item { ReviewsSection(product) }

            // Bottom Spacer
            item { Spacer(modifier = Modifier.height(32.dp)) }
        }
    }
}

@Composable
fun DetailTopAppBar(onBackClick: () -> Unit) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onBackClick) {
            Icon(Icons.Default.ArrowBackIosNew, contentDescription = "Back", tint = TextDark)
        }
        Row {
            IconButton(onClick = {}) {
                Icon(Icons.Default.Share, contentDescription = "Share", tint = TextDark)
            }
            IconButton(onClick = {}) {
                Icon(
                        Icons.Outlined.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = TextDark
                )
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductImageGallery(images: List<String>) {
    val pagerState = rememberPagerState(pageCount = { if (images.isEmpty()) 1 else images.size })

    Column(
            modifier = Modifier.fillMaxWidth().height(400.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(state = pagerState, modifier = Modifier.fillMaxWidth().weight(1f)) { page ->
            AsyncImage(
                    model =
                            if (images.isNotEmpty()) images[page]
                            else "https://via.placeholder.com/400",
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
            )
        }

        // Pager Indicator
        Row(
                Modifier.height(40.dp).fillMaxWidth().padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color =
                        if (pagerState.currentPage == iteration) PrimaryBlue else Color.LightGray
                Box(
                        modifier =
                                Modifier.padding(4.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .size(
                                                if (pagerState.currentPage == iteration) 24.dp
                                                else 8.dp
                                        )
                                        .height(8.dp)
                )
            }
        }
    }
}

@Composable
fun ProductHeader(product: Product) {
    Column(modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp, vertical = 8.dp)) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
        ) {
            Text(
                    text = product.name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextDark,
                    modifier = Modifier.weight(1f)
            )
            Column(horizontalAlignment = Alignment.End) {
                Text(
                        text = "$${product.price}",
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold,
                        color = PrimaryBlue
                )
                Surface(
                        color = if (product.quantity > 0) Color(0xFFE8F5E9) else Color(0xFFFFEBEE),
                        shape = RoundedCornerShape(4.dp),
                        modifier = Modifier.padding(top = 4.dp)
                ) {
                    Text(
                            text =
                                    if (product.quantity > 0) "${product.quantity} in stock"
                                    else "Out of stock",
                            color =
                                    if (product.quantity > 0) Color(0xFF2E7D32)
                                    else Color(0xFFC62828),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            repeat(5) { index ->
                Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint =
                                if (index < (product.rating ?: 0.0).toInt()) Amber500
                                else Color.LightGray,
                        modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                    text = "${product.rating ?: 0.0} (120 reviews)",
                    fontSize = 14.sp,
                    color = TextGrey
            )
            Icon(
                    Icons.Outlined.ArrowForwardIos,
                    contentDescription = null,
                    modifier = Modifier.size(12.dp),
                    tint = TextGrey
            )
        }
    }
}

@Composable
fun ColorSelectionSection() {
    val colors = listOf(Color(0xFF2D2D2D), Color(0xFF9BABBC), Color(0xFFDCC8A9), Color(0xFF4C5E4B))
    var selectedColor by remember { mutableIntStateOf(0) }

    Column(modifier = Modifier.padding(20.dp)) {
        Text("COLOR", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextGrey)
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
            colors.forEachIndexed { index, color ->
                Box(
                        modifier =
                                Modifier.size(40.dp)
                                        .clip(CircleShape)
                                        .background(color)
                                        .border(
                                                width = if (selectedColor == index) 2.dp else 0.dp,
                                                color =
                                                        if (selectedColor == index) PrimaryBlue
                                                        else Color.Transparent,
                                                shape = CircleShape
                                        )
                                        .padding(if (selectedColor == index) 4.dp else 0.dp)
                                        .clickable { selectedColor = index }
                ) {
                    if (selectedColor == index) {
                        Box(
                                modifier =
                                        Modifier.fillMaxSize()
                                                .clip(CircleShape)
                                                .border(2.dp, Color.White, CircleShape)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun SizeSelectionSection() {
    val sizes = listOf("S", "M (Selected)", "L")
    var selectedSize by remember { mutableIntStateOf(1) }

    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("SIZE", fontWeight = FontWeight.Bold, fontSize = 12.sp, color = TextGrey)
            Text(
                    "Size Guide",
                    color = PrimaryBlue,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
            sizes.forEachIndexed { index, size ->
                Surface(
                        modifier =
                                Modifier.weight(1f).height(48.dp).clickable {
                                    selectedSize = index
                                },
                        shape = RoundedCornerShape(12.dp),
                        color = if (selectedSize == index) PrimaryBlue else BackgroundLight,
                        border =
                                if (selectedSize == index) null
                                else BorderStroke(1.dp, Color(0xFFE5E7EB))
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                                text = size,
                                color = if (selectedSize == index) Color.White else TextDark,
                                fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun DescriptionSection(description: String) {
    var isExpanded by remember { mutableStateOf(false) }

    Column(modifier = Modifier.padding(20.dp)) {
        Text("Description", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextDark)
        Spacer(modifier = Modifier.height(12.dp))
        Text(
                text = description,
                color = TextGrey,
                lineHeight = 22.sp,
                maxLines = if (isExpanded) Int.MAX_VALUE else 3,
                overflow = TextOverflow.Ellipsis
        )
        Text(
                text = if (isExpanded) "Read less" else "Read more",
                color = PrimaryBlue,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 8.dp).clickable { isExpanded = !isExpanded }
        )
    }
}

@Composable
fun ReviewsSection(product: Product) {
    Column(modifier = Modifier.padding(horizontal = 20.dp)) {
        Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Reviews", fontWeight = FontWeight.Bold, fontSize = 20.sp, color = TextDark)
            TextButton(onClick = {}) {
                Text("See all", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                Icon(
                        Icons.Outlined.ArrowForwardIos,
                        null,
                        modifier = Modifier.size(14.dp),
                        tint = PrimaryBlue
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Rating Summary Card
        Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = BackgroundLight
        ) {
            Row(
                    modifier = Modifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("${product.rating ?: 0.0}", fontSize = 36.sp, fontWeight = FontWeight.Bold)
                    Row {
                        repeat(5) { index ->
                            Icon(
                                    Icons.Default.Star,
                                    null,
                                    modifier = Modifier.size(12.dp),
                                    tint =
                                            if (index < (product.rating ?: 0.0).toInt()) Amber500
                                            else Color.LightGray
                            )
                        }
                    }
                    Text("120 reviews", fontSize = 12.sp, color = TextGrey)
                }

                Spacer(modifier = Modifier.width(24.dp))

                Column(modifier = Modifier.weight(1f)) {
                    RatingBar(5, 0.8f)
                    RatingBar(4, 0.2f)
                    RatingBar(3, 0.1f)
                    RatingBar(2, 0.05f)
                    RatingBar(1, 0.05f)
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Single Review Example
        ReviewComponent(
                name = "Sarah M.",
                rating = 5,
                date = "2 days ago",
                comment =
                        "Absolutely love this backpack! The materials feel premium and it fits my 16\" MacBook Pro perfectly. Highly recommend for daily commute."
        )
    }
}

@Composable
fun RatingBar(stars: Int, percentage: Float) {
    Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Text("$stars", fontSize = 12.sp, color = TextGrey, modifier = Modifier.width(12.dp))
        Spacer(modifier = Modifier.width(8.dp))
        Box(
                modifier =
                        Modifier.weight(1f)
                                .height(6.dp)
                                .clip(CircleShape)
                                .background(Color(0xFFE5E7EB))
        ) {
            Box(
                    modifier =
                            Modifier.fillMaxWidth(percentage)
                                    .fillMaxHeight()
                                    .clip(CircleShape)
                                    .background(PrimaryBlue)
            )
        }
    }
}

@Composable
fun ReviewComponent(name: String, rating: Int, date: String, comment: String) {
    Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = BackgroundLight
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(modifier = Modifier.size(32.dp).clip(CircleShape).background(Color.LightGray))
                Spacer(modifier = Modifier.width(12.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(name, fontWeight = FontWeight.Bold, fontSize = 14.sp)
                    Row {
                        repeat(5) { index ->
                            Icon(
                                    Icons.Default.Star,
                                    null,
                                    modifier = Modifier.size(12.dp),
                                    tint = if (index < rating) Amber500 else Color.LightGray
                            )
                        }
                    }
                }
                Text(date, fontSize = 12.sp, color = TextGrey)
            }
            Spacer(modifier = Modifier.height(12.dp))
            Text(comment, fontSize = 14.sp, color = TextDark, lineHeight = 20.sp)
        }
    }
}

@Composable
fun StickyAddtoCartBar(product: Product, onAddToCartClick: () -> Unit) {
    Surface(modifier = Modifier.fillMaxWidth(), shadowElevation = 16.dp, color = Color.White) {
        Row(
                modifier = Modifier.padding(20.dp).fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text("Total Price", fontSize = 12.sp, color = TextGrey)
                Text("$${product.price}", fontSize = 20.sp, fontWeight = FontWeight.Bold)
            }

            Button(
                    onClick = onAddToCartClick,
                    modifier = Modifier.height(56.dp).fillMaxWidth(0.7f),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Outlined.ShoppingCart, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Add to Cart", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
