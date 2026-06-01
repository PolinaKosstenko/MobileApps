package com.example.quiz.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.harrypotterapi.R

val HarryPotterFontFamily = FontFamily(
    Font(R.font.harrypotter, FontWeight.Normal),
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Normal, fontSize = 16.sp, lineHeight = 24.sp),
    bodyMedium = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),
    labelLarge = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Normal, fontSize = 14.sp, lineHeight = 20.sp),  // Button
    labelMedium = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Medium, fontSize = 12.sp, lineHeight = 16.sp), // Badge
    labelSmall = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Medium, fontSize = 11.sp, lineHeight = 16.sp),  // NavBar
    titleLarge = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Bold, fontSize = 22.sp, lineHeight = 28.sp),
    titleMedium = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Bold, fontSize = 16.sp, lineHeight = 24.sp),
    titleSmall = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Medium, fontSize = 14.sp, lineHeight = 20.sp),
    headlineLarge = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Bold, fontSize = 32.sp, lineHeight = 40.sp),
    headlineMedium = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Bold, fontSize = 28.sp, lineHeight = 36.sp),
    headlineSmall = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Bold, fontSize = 24.sp, lineHeight = 32.sp),
    displayLarge = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Normal, fontSize = 57.sp, lineHeight = 64.sp),
    displayMedium = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Normal, fontSize = 45.sp, lineHeight = 52.sp),
    displaySmall = TextStyle(
        fontFamily = HarryPotterFontFamily,
        fontWeight = FontWeight.Normal, fontSize = 36.sp, lineHeight = 44.sp)
)
