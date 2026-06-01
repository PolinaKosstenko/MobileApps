package com.example.harrypotterapi.ui.widget

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.Grade
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.model.Spell

@Composable
fun SpellItem(
    spell: Spell,
    grade: Grade,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width((LocalConfiguration.current.screenWidthDp * 0.7).dp),

        colors = CardDefaults.cardColors(
            containerColor =
                if (spell.image != null)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
        ),

        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    enabled = spell.image != null,
                    onClick = onClick
                )
                .padding(12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = spell.name,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 10.dp)
                )
                Text(text = spell.description)
                Text(
                    modifier = Modifier.padding(top = 10.dp).fillMaxWidth(),
                    textAlign = TextAlign.End,
                    fontStyle = FontStyle.Italic,
                    text = if (spell.image != null)
                        when (grade) {
                            Grade.OUTSTANDING -> "Превосходно!"
                            Grade.EXCEEDS_EXPECTATIONS -> "Выше Ожидаемого"
                            Grade.ACCEPTABLE -> "Удовлетворительно"
                            Grade.POOR -> "Слабо"
                            Grade.DREADFUL -> "Отвратительно"
                            Grade.TROLL -> "Тролль"
                            else -> { "Доступно для изучения" }
                        }
                    else "Недоступно"
                )
            }
        }
    }
}
