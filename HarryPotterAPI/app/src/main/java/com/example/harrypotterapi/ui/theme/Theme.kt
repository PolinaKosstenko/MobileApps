package com.example.quiz.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

// Light theme variants
val GryffindorLightScheme = lightColorScheme(
    primary = Color(0xFF740001),
    secondary = Color(0xFFD3A625),
    tertiary = Color(0xFFAE0001),
    background = Color(0xFFFFF8E7),
    surface = Color(0xFFFFFBF0),
    onPrimary = Color.White,
    onSecondary = Color(0xFF2D0A0A),
    onBackground = Color(0xFF2D0A0A),
    onSurface = Color(0xFF2D0A0A),
)

val SlytherinLightScheme = lightColorScheme(
    primary = Color(0xFF1A472A),
    secondary = Color(0xFF2A623D),
    tertiary = Color(0xFF5D5D5D),
    background = Color(0xFFF0F8F2),
    surface = Color(0xFFF5FFF7),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF0A1A0F),
    onSurface = Color(0xFF0A1A0F),
)

val RavenclawLightScheme = lightColorScheme(
    primary = Color(0xFF0E1A40),
    secondary = Color(0xFF946B2D),
    tertiary = Color(0xFF5D5D5D),
    background = Color(0xFFF0F4FF),
    surface = Color(0xFFF5F8FF),
    onPrimary = Color.White,
    onSecondary = Color.White,
    onBackground = Color(0xFF0E1A40),
    onSurface = Color(0xFF0E1A40),
)

val HufflepuffLightScheme = lightColorScheme(
    primary = Color(0xFFECB939),
    secondary = Color(0xFF726258),
    tertiary = Color(0xFF372E29),
    background = Color(0xFFFFFDF0),
    surface = Color(0xFFFFFBF0),
    onPrimary = Color(0xFF372E29),
    onSecondary = Color(0xFFECB939),
    onBackground = Color(0xFF372E29),
    onSurface = Color(0xFF372E29),
)

// Neutral/Default
val NeutralDarkScheme = darkColorScheme(
    primary = Color(0xFF6650A4),
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF7D5260),
)

val NeutralLightScheme = lightColorScheme(
    primary = Color(0xFF6650A4),
    secondary = Color(0xFF625B71),
    tertiary = Color(0xFF7D5260),
)

enum class ThemeStyle {
    NEUTRAL,
    GRYFFINDOR,
    SLYTHERIN,
    RAVENCLAW,
    HUFFLEPUFF
}

@Composable
fun MainTheme(
    style: ThemeStyle,
    content: @Composable () -> Unit,
) {
    val colorScheme = when (style) {
        ThemeStyle.GRYFFINDOR -> GryffindorLightScheme
        ThemeStyle.SLYTHERIN -> SlytherinLightScheme
        ThemeStyle.RAVENCLAW -> RavenclawLightScheme
        ThemeStyle.HUFFLEPUFF -> HufflepuffLightScheme
        else -> NeutralLightScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
