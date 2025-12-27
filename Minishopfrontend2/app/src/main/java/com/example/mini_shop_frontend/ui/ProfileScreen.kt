package com.example.mini_shop_frontend.ui

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
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.mini_shop_frontend.ui.theme.*
import com.example.mini_shop_frontend.utils.UserContext
import com.example.mini_shop_frontend.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(viewModel: MainViewModel, onBackClick: () -> Unit, onLogoutClick: () -> Unit) {
    val userProfile by viewModel.userProfile.collectAsState()

    Scaffold(
            topBar = {
                TopAppBar(
                        title = {
                            Box(
                                    modifier = Modifier.fillMaxWidth(),
                                    contentAlignment = Alignment.Center
                            ) { Text("Profile", fontWeight = FontWeight.Bold, fontSize = 18.sp) }
                        },
                        navigationIcon = {
                            IconButton(onClick = onBackClick) {
                                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                            }
                        },
                        actions = {
                            TextButton(onClick = { /* Edit action */}) {
                                Text("Edit", color = PrimaryBlue, fontWeight = FontWeight.Bold)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White),
                        windowInsets = WindowInsets(0.dp)
                )
            },
            containerColor = Color(0xFFF9FAFB),
            contentWindowInsets = WindowInsets(0.dp)
    ) { paddingValues ->
        LazyColumn(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                horizontalAlignment = Alignment.CenterHorizontally,
                contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            // Header Section
            item {
                ProfileHeader(
                        name = userProfile?.username ?: "Loading...",
                        email = userProfile?.email ?: "...",
                        membershipLevel = userProfile?.membershipLevel ?: "REGULAR"
                )
            }

            // Stats Section
            item {
                StatsSection(
                        points = userProfile?.points ?: 0,
                        vouchers = userProfile?.vouchers ?: 0,
                        reviews = 0 // We don't have reviews yet
                )
            }

            // Menu Sections
            item {
                MenuSection(title = "MY SHOPPING") {
                    MenuItem(icon = Icons.Default.Inventory2, label = "My Orders", onClick = {})
                    OrderPreviews()

                    HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color(0xFFF3F4F6)
                    )

                    MenuItem(icon = Icons.Default.Favorite, label = "Wishlist", onClick = {})
                    WishlistPreviews()
                }
            }

            item {
                MenuSection(title = "ACCOUNT") {
                    MenuItem(icon = Icons.Default.LocationOn, label = "Address Book", onClick = {})
                    HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color(0xFFF3F4F6)
                    )
                    MenuItem(
                            icon = Icons.Default.CreditCard,
                            label = "Payment Methods",
                            onClick = {}
                    )
                }
            }

            item {
                MenuSection(title = "SYSTEM") {
                    MenuItem(icon = Icons.Default.Settings, label = "Settings", onClick = {})
                    HorizontalDivider(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            color = Color(0xFFF3F4F6)
                    )
                    MenuItem(icon = Icons.Default.Help, label = "Help & Support", onClick = {})
                }
            }

            // Logout Button
            item {
                Button(
                        onClick = {
                            onLogoutClick()
                        },
                        modifier = Modifier.fillMaxWidth().padding(HorizontalPadding).height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors =
                                ButtonDefaults.buttonColors(
                                        containerColor = Color(0xFFFEF2F2),
                                        contentColor = Color(0xFFEF4444)
                                )
                ) { Text("Log Out", fontWeight = FontWeight.Bold, fontSize = 16.sp) }

                Text(
                        text = "App Version 2.4.0",
                        color = TextGrey,
                        fontSize = 12.sp,
                        modifier = Modifier.padding(top = 8.dp)
                )
            }
        }
    }
}

@Composable
private fun ProfileHeader(name: String, email: String, membershipLevel: String) {
    Column(
            modifier = Modifier.fillMaxWidth().padding(vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(contentAlignment = Alignment.BottomEnd) {
            // Avatar (Placeholder for now)
            Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = Color(0xFFE5E7EB)
            ) {
                Icon(
                        Icons.Default.Person,
                        contentDescription = null,
                        modifier = Modifier.padding(20.dp),
                        tint = Color.Gray
                )
            }

            // Edit Badge
            Surface(
                    modifier = Modifier.size(28.dp),
                    shape = CircleShape,
                    color = PrimaryBlue,
                    border = androidx.compose.foundation.BorderStroke(2.dp, Color.White)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                            Icons.Default.Edit,
                            contentDescription = "Edit Avatar",
                            tint = Color.White,
                            modifier = Modifier.size(14.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        Text(name, fontWeight = FontWeight.Bold, fontSize = 24.sp, color = TextDark)
        Text(email, color = TextGrey, fontSize = 14.sp)

        Spacer(modifier = Modifier.height(8.dp))
        Surface(color = Color(0xFFFEF9C3), shape = RoundedCornerShape(20.dp)) {
            Text(
                    "$membershipLevel MEMBER",
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp),
                    color = Color(0xFF854D0E),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StatsSection(points: Int, vouchers: Int, reviews: Int) {
    Row(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        StatCard(value = points.toString(), label = "Points", modifier = Modifier.weight(1f))
        StatCard(value = vouchers.toString(), label = "Vouchers", modifier = Modifier.weight(1f))
        StatCard(value = reviews.toString(), label = "Reviews", modifier = Modifier.weight(1f))
    }
}

@Composable
private fun StatCard(value: String, label: String, modifier: Modifier = Modifier) {
    Card(
            modifier = modifier,
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                    value,
                    fontWeight = FontWeight.Black,
                    fontSize = 20.sp,
                    color = PrimaryBlue,
                    textAlign = TextAlign.Center
            )
            Text(label, fontSize = 12.sp, color = TextGrey, textAlign = TextAlign.Center)
        }
    }
}

@Composable
private fun MenuSection(title: String, content: @Composable ColumnScope.() -> Unit) {
    Column(modifier = Modifier.padding(top = 24.dp)) {
        Text(
                text = title,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = TextGrey
        )
        Card(
                modifier = Modifier.padding(horizontal = 16.dp),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) { Column(content = content) }
    }
}

@Composable
private fun MenuItem(icon: ImageVector, label: String, onClick: () -> Unit) {
    Row(
            modifier = Modifier.fillMaxWidth().clickable { onClick() }.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = Color(0xFFF3F4F6)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                        icon,
                        contentDescription = null,
                        modifier = Modifier.size(20.dp),
                        tint = TextDark
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Text(label, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = TextDark)
        Spacer(modifier = Modifier.weight(1f))
        Icon(Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
    }
}

@Composable
private fun OrderPreviews() {
    LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(3) { index ->
            Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = if (index == 0) Color(0xFF5A8B5D) else Color(0xFFF3F4F6)
            ) {
                // Placeholder images
                if (index == 0) {
                    Box(contentAlignment = Alignment.Center) {
                        Text("Order\n1", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            }
        }
    }
}

@Composable
private fun WishlistPreviews() {
    LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(3) { _ ->
            Surface(
                    modifier = Modifier.size(80.dp),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFF3F4F6)
            ) {
                // Placeholder images
            }
        }
    }
}

private val HorizontalPadding = 16.dp
