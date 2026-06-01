package com.example.harrypotterapi.ui.widget

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.example.harrypotterapi.R
import com.example.harrypotterapi.model.House

@Composable
fun Background(imageRes: Int, content: @Composable () -> Unit) {
    Box(modifier = Modifier.fillMaxSize()) {
        if (imageRes != -1) {
            Image(
                painter = painterResource(imageRes),
                contentDescription = "background",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
        }

        content()
    }
}
