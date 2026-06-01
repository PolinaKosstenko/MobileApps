package com.example.harrypotterapi.model

import com.example.quiz.ui.theme.ThemeStyle
import kotlinx.serialization.Serializable

enum class Grade {
    OUTSTANDING,
    EXCEEDS_EXPECTATIONS,
    ACCEPTABLE,
    POOR,
    DREADFUL,
    TROLL,
    NO_GRADE
}

@Serializable
data class User(
    val characterId: Int,
    val grades: Map<String, Grade>,
    val selectedLocation: House,
    val selectedTheme: ThemeStyle = ThemeStyle.NEUTRAL
)
