package com.example.harrypotterapi.ui.widget

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun LoadingView() {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary),
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = "Загрузка...",
        )
    }
}

@Composable
fun ErrorView(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary),
    ) {
        Text(
            modifier = Modifier.padding(10.dp),
            text = errorMessage,
            color = Color.Red
        )
    }

    Button(
        modifier = Modifier.fillMaxWidth(),
        onClick = onRetry,
    ) {
        Text("Попробовать заново")
    }
}
