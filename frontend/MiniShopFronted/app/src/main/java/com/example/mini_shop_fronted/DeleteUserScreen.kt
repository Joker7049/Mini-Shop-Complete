package com.example.mini_shop_fronted

import androidx.compose.runtime.*
import com.example.mini_shop_fronted.ui.components.DeleteUserContent
import com.example.mini_shop_fronted.viewmodel.AdminActionState
import com.example.mini_shop_fronted.viewmodel.MainViewModel

@Composable
fun DeleteUserScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    var username by remember { mutableStateOf("") }
    val adminActionState by viewModel.adminActionState.collectAsState()

    LaunchedEffect(adminActionState) {
        if (adminActionState is AdminActionState.Success) {
            viewModel.resetAdminActionState()
            onBack()
        }
    }

    DeleteUserContent(
            username = username,
            onUsernameChange = { username = it },
            onDelete = {
                if (username.isNotBlank()) {
                    viewModel.deleteUser(username)
                }
            },
            adminActionState = adminActionState,
            onBack = onBack
    )
}
