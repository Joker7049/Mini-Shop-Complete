package com.example.mini_shop_fronted

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
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
                onValueChange = {
                    name = it
                    viewModel.productNameError = null
                },
                label = { Text("Product Name") },
                supportingText = {
                    if (viewModel.productNameError != null) {
                        Text(
                                text = viewModel.productNameError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        DescriptionFieldWithAI(
                description = description,
                onDescriptionChange = {
                    description = it
                    viewModel.productDescriptionError = null
                },
                ollamaState = ollamaState,
                onGenerateDescription = { viewModel.getOllamaProductDescription(name) },
                descriptionError = viewModel.productDescriptionError,
                modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
                value = price,
                onValueChange = {
                    price = it
                    viewModel.productPriceError = null
                },
                label = { Text("Price") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    if (viewModel.productPriceError != null) {
                        Text(
                                text = viewModel.productPriceError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(8.dp))

        OutlinedTextField(
                value = quantity,
                onValueChange = {
                    quantity = it
                    viewModel.productQuantityError = null
                },
                label = { Text("Quantity") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                supportingText = {
                    if (viewModel.productQuantityError != null) {
                        Text(
                                text = viewModel.productQuantityError!!,
                                color = MaterialTheme.colorScheme.error,
                                fontWeight = FontWeight.Bold
                        )
                    }
                },
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
                        /*if (name.isNotBlank() && priceVal != null && quantityVal != null) {
                            viewModel.addProduct(name, description, priceVal, quantityVal)
                        }*/
                        viewModel.addProduct(name, description, priceVal ?: -3.3, quantityVal ?: -3)
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
