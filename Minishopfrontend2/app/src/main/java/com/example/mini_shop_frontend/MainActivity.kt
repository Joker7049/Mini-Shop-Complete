package com.example.mini_shop_frontend

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material.icons.outlined.Chair
import androidx.compose.material.icons.outlined.Checkroom
import androidx.compose.material.icons.outlined.Devices
import androidx.compose.material.icons.outlined.Diamond
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.FitnessCenter
import androidx.compose.material.icons.outlined.GridView
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Typography
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.example.mini_shop_frontend.viewmodel.MainViewModel

// --- Constants & Color Palette from Tailwind Config ---
val PrimaryBlue = Color(0xFF137FEC)
val BackgroundLight = Color(0xFFF6F7F8)
val BackgroundDark = Color(0xFF101922)
val TextDark = Color(0xFF111418)
val TextGrey = Color(0xFF617589)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { ShopperTheme { ShopperApp() } }
    }
}

// --- Data Models ---
data class HeroBanner(
    val title: String,
    val subtitle: String,
    val imageUrl: String,
    val tag: String,
    val buttonText: String
)

data class Product(
    val name: String,
    val imageUrl: String,
    val rating: Double?,
    val price: Double,
    val oldPrice: Double? = null,
    val category: String? = null,
    val discountTag: String? = null,
    val isBestSeller: Boolean = false
)

data class CategoryItem(val name: String, val icon: ImageVector)

// --- Mock Data ---
val heroBanners =
    listOf(
        HeroBanner(
            "Summer\nCollection",
            "Up to 50% Off selected items",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuB2xo55kXlRYDAd4StnFB04Spq8l8f3mBm1ocaj74zOqQQx9VUIyAFbLPXTeoMaLOyszs57wyIzp8XQPCf04NCWDMXxACkkBaxsB6YQ71lJKnfmPTHQC0BcGa_JE9xycTfBnCEe2zm_kub5y9r90RR2AghZduz-Vjgno9XY9fI6ZVSoaZrrNYXtWRg-eIfriygqJxs9ut4mhVmxRHmxbTGVO-JM0S5Pk2stB1Cio1ngXz0adkVIaS8Nm4v6Tix3u4Ow5tKcjB57LrQ",
            "NEW SEASON",
            "Shop Now"
        ),
        HeroBanner(
            "Latest\nGadgets",
            "Experience the future today",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuAYZ74e5wPKMcDopHJD0xzdcvld8XqcsqR0ZWZwgs2ie_lxj-6bzxIn20JleWAwaYZbm7UkmGkSVv8J_SS06Y9VQuTI6y-by5_OkBe00VSy-wfisLewRHcjydEFgN6muOipr2voCOkqZ_PN2BqLWRSwW6h0bI8LCBNi6ZqfhCyJCNvgK_NS_CTwWUhUh3XwU8f1VN1EZ-hExADF0pRVYZcV_0y2t-AiyCZtM-L2IRGOly2jYj5tt8CfRdoFEgZ1PIh5l0TM6tJmnAw",
            "TECH",
            "Explore"
        )
    )

val categories =
    listOf(
        CategoryItem("Fashion", Icons.Outlined.Checkroom),
        CategoryItem("Tech", Icons.Outlined.Devices),
        CategoryItem("Home", Icons.Outlined.Chair),
        CategoryItem("Sports", Icons.Outlined.FitnessCenter),
        CategoryItem("Beauty", Icons.Outlined.Diamond)
    )

val newArrivals =
    listOf(
        Product(
            "Nike Air Max Red",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuAJgvAvwaR90x5rBMmX0JfpA6OWKFWaKSBejXsD5fxRiWt3YgnWY__7P2uR-7ILcYWNRMW6YVujFjJbmNCB5LzFs-_H1Zz8q8OSAdfn0TRIe1m1_jNPHCDZDZMp6N1JreFKa0CuNgUnAN2kaAPzLUyxxcNme7Kic6JeLsV_Fj7RcMG21O1M8bd-enZqkpPWh6RaYQ1NEu11ss24iPcFZ5RAqFZAsuuch_QUuPoZBfYlKcRMkHNPaPsA8NdXFHjVnTAj_GQf1GcYvEI",
            4.8,
            129.00
        ),
        Product(
            "Sony Headphones",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuBNgoKNaT2CoHAmiwAsJ32iFjtSqEwfUwtllj-vvD3XK_zoJF0zVJAK6NeGv19EiH8wj5PNpiI9IJjL9DiNmfgsoN7ug-ok3TYPlTpEuP0rRSHYd3wuaK55vBNi8veEXaTgTRLnybhc8vxB7O40aE4_cBFvgYoZnukD57KieILgIjJ66B_iBYDiGtiRqi6q__Vb0jnVy7WMuBOo0It9-PKqBUqU413_gDC7HK4u82koYD63uqD-UsyGD1uioVLKtaURUg5Ilbb7XrE",
            4.9,
            299.00
        ),
        Product(
            "Smart Watch Pro",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuChXXhht-_YrcEWg8I9nUf5JnpzLCzkrLymVYZa56iort3eNI_4yBvYH9EghqBL0Bd5sfT9zuOMz1XYOBXq1XX7sQduz5WGm5kooHgxhHDUj4ZLjuTvNwTVOso_v6sxNZsJPhK0h-HlMHbo5zLJTBccCWSEvPt3JhU6Hc4_Zn0d68bxCy3DI3N8W-fWNQ6gpyXI_XQR96uX_KolpYwP7MbwuNlaxsjpKLD4tANd8IBVp33HeOkF48c2MN2TqyavSugniqpwFtN1UVM",
            4.5,
            199.00
        )
    )

val trendingProducts =
    listOf(
        Product(
            "Classic Leather Bag",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuCvLfDem5hN9RGwCO62FFlFswaUh5TuzLGQK8cCX9pM_Wcl4oRENwAJ8WKwwARQjHOxQVGZPdAMjzZ-HQqufhpifzVOMoP8qmxlYAFXzyVtSHRil7kDqeIQlC--W7BORq4zK071UQxgEUNo0MBIRoAcXFtPKqlXmsmByoSk-CGWR4s8S4WD3rMUvG8Rpb2JPQ_ASCHlbnAjG2m4CCGcxars7FKkUCDkHTPEdUD6PZR9Kos7Ebc0p6WxL0W_pBxAO38pQ48TVp2Tn-E",
            null,
            89.00,
            category = "Accessories",
            isBestSeller = true
        ),
        Product(
            "Basic Black Tee",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuApn5y5g4qZ81lBLLxa3zerSiIfIaIk6-qdx0Zh87gQSG7W3hEmwh_V-43VUfg17yyErvN_jK5cqEgJsvUjh0lOVLuMegOOhJyYWTJc13IhlSmHwTVWyYuvPTc9HDK9epJTYIxdjgGA94gEL3SjaBaT5ghQsN-v3pEvYoKbYtXSxlcdqDD6yCpkt8aYguX797SK-Pe-WRqZyOzAvy2wfGgkCimen508vMBDIWUPiTtGgY5CBSW7o4QgWlINr-e8Vc9kLb_6f_wxrU8",
            null,
            24.00,
            category = "Clothing"
        ),
        Product(
            "Urban Sneakers",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuA7bNJ-a80lVaCYYHxPzjMbS53Pbl4ijvJ35ZXVMAUX5Y-Qj84gQJOieu8xTEFv0rqLTHIyknTEsqyc_8AyplsBL-jQIoI1T8Yp1IsBm52AzeFYNps6GAcH2-vraRJF3NlUL3Pr3MRJunkEap1uGMnLkL0srxO8SKJHtE32gompEI-hvWjEgI7WhPR7WKn45dRhdAZG_X_cdsIAQDEY4Hjsyg_9GGQ6wQtnf6TmDCh_Fpv9uiy-GAuc0soziUUwMWEO9tnneRipbaY",
            null,
            75.00,
            category = "Footwear"
        ),
        Product(
            "Essence Perfume",
            "https://lh3.googleusercontent.com/aida-public/AB6AXuAFKXgTJH_xoi2bGqzHffWlz0is7wUpVxNLGIKwcJHq3hzfv5qZYuLKIyr-Tmzk9o_j46i_yBmeYUQIMc0sDCi8tzyAandFxSNgCmsP5-4oBNBeIMshH-OgjvilsC7TP6idpHVRXXZJ1cDMre7UTea_6vSfht83IyeolPzs77546zZ-wa3waav1Qj9PcHq8Y1ew4t2kBQnhROIS-Q9205pL27PJllx4Qdezu7blCmZMYphkuhTiow2Ud-YzWXDXc3CuXWQt8TbTiN8",
            null,
            95.00,
            oldPrice = 120.00,
            category = "Beauty",
            discountTag = "-20%"
        )
    )

// --- Composable: Main App Structure ---
@Composable
fun ShopperApp(viewModel: MainViewModel = viewModel()) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }

    Scaffold(
        bottomBar = { BottomNavigationBar(selectedTab) { selectedTab = it } },
        containerColor = BackgroundLight
    ) { paddingValues ->
        // Main Content List
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Sticky Header
            stickyHeader { TopAppBar() }

            // Search Bar (Not sticky in this design implementation, mimics the HTML flow)
            item { SearchBar(modifier = Modifier.padding(horizontal = 16.dp)) }

            // Hero Carousel
            item { HeroCarousel() }

            // Categories
            item { CategorySection() }

            // New Arrivals (Real Data)
            item {
                if (isLoading) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator() }
                } else if (error != null) {
                    Text("Error: $error", color = Color.Red, modifier = Modifier.padding(16.dp))
                } else {
                    NewArrivalsSection(products)
                }
            }

            // Trending Grid
            // We implement the 2-column grid by chunking the list and rendering rows manually
            // to avoid nested scrolling issues with LazyVerticalGrid inside LazyColumn
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Trending Now",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = TextDark
                    )
                }
            }

            items(trendingProducts.chunked(2)) { rowItems ->
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    rowItems.forEach { product ->
                        Box(modifier = Modifier.weight(1f)) { TrendingProductCard(product) }
                    }
                    // If row has only 1 item, add empty weight to balance
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }

            // Spacer for bottom nav visibility
            item { Spacer(modifier = Modifier.height(16.dp)) }
        }
    }
}

// --- Components ---

@Composable
fun TopAppBar() {
    Surface(color = Color.White, shadowElevation = 2.dp, modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp, vertical = 12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            // Menu Button
            IconButton(
                onClick = {},
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.Transparent, CircleShape)
            ) { Icon(Icons.Default.Menu, contentDescription = "Menu", tint = TextDark) }

            // Title
            Text(
                text = "SHOPPER",
                fontSize = 20.sp,
                fontWeight = FontWeight.ExtraBold,
                color = TextDark,
                modifier = Modifier.weight(1f),
                textAlign = androidx.compose.ui.text.style.TextAlign.Center
            )

            // Actions
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Box {
                    IconButton(onClick = {}, modifier = Modifier.size(40.dp)) {
                        Icon(
                            Icons.Outlined.ShoppingCart,
                            contentDescription = "Cart",
                            tint = TextDark
                        )
                    }
                    // Red Dot
                    Box(
                        modifier =
                            Modifier
                                .align(Alignment.TopEnd)
                                .padding(top = 4.dp, end = 4.dp)
                                .size(10.dp)
                                .background(PrimaryBlue, CircleShape)
                                .border(2.dp, Color.White, CircleShape)
                    )
                }
                IconButton(onClick = {}, modifier = Modifier.size(40.dp)) {
                    Icon(
                        Icons.Outlined.AccountCircle,
                        contentDescription = "Profile",
                        tint = TextDark
                    )
                }
            }
        }
    }
}

@Composable
fun SearchBar(modifier: Modifier = Modifier) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp),
        shape = RoundedCornerShape(8.dp),
        color = Color(0xFFF0F2F4)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(horizontal = 12.dp)
        ) {
            Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color.Gray)
            Spacer(modifier = Modifier.width(12.dp))
            Text(
                text = "Find your favorite items...",
                color = TextGrey,
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HeroCarousel() {
    val pagerState = rememberPagerState(pageCount = { heroBanners.size })

    // Using simple Row with horizontal scroll for specific "snap" feel requested by design
    // However, HorizontalPager is more idiomatic for carousels.
    // Let's use LazyRow with snap behavior to match the HTML `snap-x` exactly.

    LazyRow(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) { items(heroBanners) { banner -> HeroCard(banner) } }
}

@Composable
fun HeroCard(banner: HeroBanner) {
    Card(
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier =
            Modifier
                .width(340.dp) // Approximate from HTML 85vw/max400px
                .height(400.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            AsyncImage(
                model = banner.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            // Gradient Overlay
            Box(
                modifier =
                    Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors =
                                    listOf(
                                        Color.Transparent,
                                        Color.Black.copy(alpha = 0.7f)
                                    ),
                                startY = 100f
                            )
                        )
            )

            // Content
            Column(modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(24.dp)) {
                // Tag
                Surface(
                    color =
                        if (banner.tag == "NEW SEASON") PrimaryBlue
                        else Color.White.copy(alpha = 0.2f),
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier.padding(bottom = 12.dp)
                ) {
                    Text(
                        text = banner.tag,
                        color = Color.White,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                Text(
                    text = banner.title,
                    color = Color.White,
                    fontSize = 32.sp,
                    lineHeight = 36.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = banner.subtitle,
                    color = Color(0xFFE5E7EB),
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(vertical = 8.dp)
                )

                Button(
                    onClick = {},
                    colors =
                        ButtonDefaults.buttonColors(
                            containerColor =
                                if (banner.buttonText == "Shop Now") Color.White
                                else PrimaryBlue,
                            contentColor =
                                if (banner.buttonText == "Shop Now") TextDark
                                else Color.White
                        ),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.padding(top = 8.dp)
                ) { Text(text = banner.buttonText, fontWeight = FontWeight.Bold) }
            }
        }
    }
}

@Composable
fun CategorySection() {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(vertical = 16.dp)) {
        Text(
            text = "Categories",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            color = TextDark,
            modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
        )

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(categories) { category ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.width(72.dp)
                ) {
                    Surface(
                        shape = CircleShape,
                        color = Color(0xFFF0F2F4),
                        modifier = Modifier
                            .size(64.dp)
                            .clickable {}
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(
                                imageVector = category.icon,
                                contentDescription = null,
                                tint = TextDark
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = category.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        color = TextDark
                    )
                }
            }
        }
    }
}

@Composable
fun NewArrivalsSection(products: List<Product>) {
    Column(modifier = Modifier
        .fillMaxWidth()
        .background(Color.White)
        .padding(bottom = 16.dp)) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "New Arrivals",
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold,
                color = TextDark
            )
            Text(
                text = "See all",
                color = PrimaryBlue,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.clickable {}
            )
        }

        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) { items(products) { product -> NewArrivalCard(product) } }
    }
}

@Composable
fun NewArrivalCard(product: Product) {
    Column(modifier = Modifier.width(144.dp)) {
        Box(
            modifier =
                Modifier
                    .aspectRatio(0.75f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F2F4))
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Surface(
                shape = CircleShape,
                color = Color.White.copy(alpha = 0.8f),
                modifier =
                    Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .size(32.dp)
                        .clickable {}
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.FavoriteBorder,
                        contentDescription = "Like",
                        modifier = Modifier.size(18.dp),
                        tint = TextDark
                    )
                }
            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = product.name,
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = TextDark,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = null,
                tint = Color(0xFFFFC107), // Amber 500
                modifier = Modifier.size(14.dp)
            )
            Text(
                text = "${product.rating}",
                fontSize = 12.sp,
                color = TextGrey,
                modifier = Modifier.padding(start = 4.dp)
            )
        }
        Text(
            text = "$${product.price}0",
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = PrimaryBlue
        )
    }
}

@Composable
fun TrendingProductCard(product: Product) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier =
                Modifier
                    .aspectRatio(1f)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFF0F2F4))
        ) {
            AsyncImage(
                model = product.imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            if (product.isBestSeller) {
                Surface(
                    color = Color.White,
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = "BEST SELLER",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }

            if (product.discountTag != null) {
                Surface(
                    color = Color(0xFFEF4444), // Red 500
                    shape = RoundedCornerShape(4.dp),
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(8.dp)
                ) {
                    Text(
                        text = product.discountTag,
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                    )
                }
            }
        }

        Column(modifier = Modifier.padding(top = 12.dp)) {
            Text(
                text = product.name,
                fontWeight = FontWeight.SemiBold,
                fontSize = 16.sp,
                color = TextDark,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(text = product.category ?: "Item", fontSize = 12.sp, color = TextGrey)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$${product.price}0",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = TextDark
                    )
                    if (product.oldPrice != null) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "$${product.oldPrice.toInt()}",
                            fontSize = 12.sp,
                            color = Color.Gray,
                            textDecoration = TextDecoration.LineThrough
                        )
                    }
                }

                Surface(
                    shape = CircleShape,
                    color = PrimaryBlue.copy(alpha = 0.1f),
                    modifier = Modifier
                        .size(32.dp)
                        .clickable {}
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BottomNavigationBar(selectedTab: Int, onTabSelected: (Int) -> Unit) {
    NavigationBar(
        containerColor = Color.White,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets(0.dp)
    ) {
        val items =
            listOf(
                Triple("Home", Icons.Default.Home, Icons.Outlined.Home),
                Triple("Catalog", Icons.Default.GridView, Icons.Outlined.GridView),
                Triple("Wishlist", Icons.Default.Favorite, Icons.Outlined.FavoriteBorder),
                Triple("Account", Icons.Default.Person, Icons.Outlined.Person)
            )

        items.forEachIndexed { index, item ->
            val isSelected = selectedTab == index
            NavigationBarItem(
                selected = isSelected,
                onClick = { onTabSelected(index) },
                icon = {
                    Icon(
                        imageVector = if (isSelected) item.second else item.third,
                        contentDescription = item.first
                    )
                },
                label = {
                    Text(text = item.first, fontSize = 10.sp, fontWeight = FontWeight.Medium)
                },
                colors =
                    NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryBlue,
                        selectedTextColor = PrimaryBlue,
                        unselectedIconColor = Color.Gray,
                        unselectedTextColor = Color.Gray,
                        indicatorColor = Color.Transparent
                    )
            )
        }
    }
}

// --- Theme Wrapper (Minimal) ---
@Composable
fun ShopperTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme =
            lightColorScheme(
                primary = PrimaryBlue,
                background = BackgroundLight,
                surface = Color.White
            ),
        typography =
            Typography(
                titleLarge =
                    androidx.compose.ui.text.TextStyle(
                        fontFamily =
                            androidx.compose.ui.text.font.FontFamily
                                .SansSerif,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
            ),
        content = content
    )
}
