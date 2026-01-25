package com.example.splitmate.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen(onStart: () -> Unit) {
    Column(
        Modifier.width((LocalConfiguration.current.screenWidthDp * 0.7).dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Расчет трат \uD83D\uDCB5\uD83D\uDC85",
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 10.dp)
        )

        Button(onClick = onStart) {
            Text(text = "Начать")
        }
    }
}
