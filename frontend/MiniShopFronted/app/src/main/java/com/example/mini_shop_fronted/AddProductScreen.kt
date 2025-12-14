package com.example.mini_shop_fronted

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.mini_shop_fronted.ui.components.DescriptionFieldWithAI
import com.example.mini_shop_fronted.viewmodel.AdminActionState
import com.example.mini_shop_fronted.viewmodel.MainViewModel
import com.example.mini_shop_fronted.viewmodel.OllamaProductDescriptionState

@Composable
fun AddProductScreen(viewModel: MainViewModel, onBack: () -> Unit) {
    var name by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var quantity by remember { mutableStateOf("") }
    val adminActionState by viewModel.adminActionState.collectAsState()
    val ollamaState by viewModel.ollamaProductDescriptionResponse.collectAsState()

    LaunchedEffect(adminActionState) {
        if (adminActionState is AdminActionState.Success) {
            viewModel.resetAdminActionState()
            onBack()
        }
    }

    LaunchedEffect(ollamaState) {
        if (ollamaState is OllamaProductDescriptionState.Success) {
            description =
                    (ollamaState as OllamaProductDescriptionState.Success).response.description
                            ?: ""
            viewModel.resetOllamaState()
        }
    }

    Column(
            modifier = Modifier.fillMaxSize().padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
    ) {
        Text(
                text = "Add Product",
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        OutlinedTextField(
                value = name,
                onValueChange = { name = it },
                label = { Text("Product Name") },
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DescriptionFieldWithAI(
                description = description,
                onDescriptionChange = { description = it },
                ollamaState = ollamaState,
                onGenerateDescription = { viewModel.getOllamaProductDescription(name) },
                modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
                value = quantity,
                onValueChange = { quantity = it },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(24.dp))

        if (adminActionState is AdminActionState.Loading) {
            CircularProgressIndicator()
        } else {
            Button(
                    onClick = {
                        val priceVal = price.toDoubleOrNull()
                        val quantityVal = quantity.toIntOrNull()
                        if (name.isNotBlank() && priceVal != null && quantityVal != null) {
                            viewModel.addProduct(name, description, priceVal, quantityVal)
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
            ) { Text("Add Product") }
        }

        if (adminActionState is AdminActionState.Error) {
            Text(
                    text = (adminActionState as AdminActionState.Error).message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = 8.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        TextButton(onClick = onBack) { Text("Cancel") }
    }
}
