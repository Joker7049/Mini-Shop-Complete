package com.example.mini_shop_fronted.ui.components

import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.mini_shop_fronted.viewmodel.OllamaProductDescriptionState

@Composable
fun DescriptionFieldWithAI(
        description: String,
        onDescriptionChange: (String) -> Unit,
        ollamaState: OllamaProductDescriptionState,
        onGenerateDescription: () -> Unit,
        modifier: Modifier = Modifier
) {
    OutlinedTextField(
            value = description,
            onValueChange = onDescriptionChange,
            label = { Text("Description") },
            modifier = modifier,
            trailingIcon = {
                if (ollamaState is OllamaProductDescriptionState.Loading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    IconButton(onClick = onGenerateDescription) {
                        Icon(
                                imageVector = Icons.Filled.Star,
                                contentDescription = "Generate Description",
                                tint = MaterialTheme.colorScheme.primary
                        )
                    }
                }
            },
            isError = ollamaState is OllamaProductDescriptionState.Error,
            supportingText = {
                if (ollamaState is OllamaProductDescriptionState.Error) {
                    Text(ollamaState.message)
                }
            }
    )
}
