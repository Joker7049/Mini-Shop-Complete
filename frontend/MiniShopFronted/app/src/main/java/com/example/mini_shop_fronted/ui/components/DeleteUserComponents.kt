package com.example.mini_shop_fronted.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mini_shop_fronted.viewmodel.AdminActionState

@Composable
fun DeleteUserContent(
        username: String,
        onUsernameChange: (String) -> Unit,
        onDelete: () -> Unit,
        adminActionState: AdminActionState,
        onBack: () -> Unit
) {
    Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Text(
                text = "Delete User",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
                value = username,
                onValueChange = onUsernameChange,
                label = { Text("Username to delete") },
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (adminActionState is AdminActionState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                    onClick = onDelete,
                    modifier = Modifier.fillMaxWidth(),
                    colors =
                            ButtonDefaults.buttonColors(
                                    containerColor = MaterialTheme.colorScheme.error
                            )
            ) { Text("Delete User") }
        }

        if (adminActionState is AdminActionState.Error) {
            Text(
                    text = adminActionState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBack) { Text("Cancel") }
    }
}
