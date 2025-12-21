package com.example.mini_shop_fronted.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.example.mini_shop_fronted.AddProductScreen
import com.example.mini_shop_fronted.AdminPanelScreen
import com.example.mini_shop_fronted.DeleteUserScreen
import com.example.mini_shop_fronted.HomeScreen
import com.example.mini_shop_fronted.LoginScreen
import com.example.mini_shop_fronted.ProductDetailScreen
import com.example.mini_shop_fronted.Screen
import com.example.mini_shop_fronted.SignUpScreen
import com.example.mini_shop_fronted.viewmodel.MainViewModel

@Composable
fun AppNavigation(navController: NavHostController, mainViewModel: MainViewModel) {
    NavHost(navController = navController, startDestination = Screen.Login) {
        composable<Screen.Login> {
            LoginScreen(
                    viewModel = mainViewModel,
                    onNavigateToSignUp = { navController.navigate(Screen.SignUp) },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.Login) { inclusive = true }
                        }
                    }
            )
        }

        composable<Screen.SignUp> {
            SignUpScreen(
                    viewModel = mainViewModel,
                    onNavigateToLogin = {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.SignUp) { inclusive = true }
                        }
                    },
                    onNavigateToHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.SignUp) { inclusive = true }
                        }
                    }
            )
        }

        composable<Screen.Home> {
            HomeScreen(
                    viewModel = mainViewModel,
                    onLogout = {
                        navController.navigate(Screen.Login) {
                            popUpTo(Screen.Home) { inclusive = true }
                        }
                    },
                    onProductClick = { product ->
                        navController.navigate(
                                Screen.ProductDetail(
                                        productId = product.id,
                                        name = product.name,
                                        description = product.description,
                                        price = product.price,
                                        quantity = product.quantity,
                                        imageUrl = product.imageUrl
                                )
                        )
                    },
                    onNavigateToAdmin = { navController.navigate(Screen.AdminPanel) }
            )
        }

        composable<Screen.ProductDetail> { backStackEntry ->
            val productDetail: Screen.ProductDetail = backStackEntry.toRoute()
            ProductDetailScreen(
                    productId = productDetail.productId,
                    name = productDetail.name,
                    description = productDetail.description,
                    price = productDetail.price,
                    quantity = productDetail.quantity,
                    imageUrl = productDetail.imageUrl,
                    viewModel = mainViewModel,
                    onBack = { navController.popBackStack() }
            )
        }

        composable<Screen.AdminPanel> {
            AdminPanelScreen(
                    viewModel = mainViewModel,
                    onNavigateHome = {
                        navController.navigate(Screen.Home) {
                            popUpTo(Screen.AdminPanel) { inclusive = true }
                        }
                    },
                    onNavigateToAddProduct = { navController.navigate(Screen.AddProduct) },
                    onNavigateToDeleteUser = { navController.navigate(Screen.DeleteUser) }
            )
        }

        composable<Screen.AddProduct> {
            AddProductScreen(viewModel = mainViewModel, onBack = { navController.popBackStack() })
        }

        composable<Screen.DeleteUser> {
            DeleteUserScreen(viewModel = mainViewModel, onBack = { navController.popBackStack() })
        }
    }
}
