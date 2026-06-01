package com.example.harrypotterapi.ui.screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.harrypotterapi.model.Grade
import com.example.harrypotterapi.model.Spell
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIViewModel
import com.example.harrypotterapi.ui.viewmodel.UiState
import com.example.harrypotterapi.ui.widget.ErrorView
import com.example.harrypotterapi.ui.widget.LoadingView
import com.example.harrypotterapi.ui.widget.SpellItem

@Composable
fun LessonsScreen(
    viewModel: HarryPotterAPIViewModel,
    grades: Map<String, Grade>,
    onSelect: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary),
        ) {
            Text(
                text = "Изучение заклинаний",
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }

        val state by viewModel.getSpellsUiState.collectAsStateWithLifecycle()

        when (val uiState = state) {
            is UiState.Loading -> LoadingView()
            is UiState.Error -> ErrorView(
                "Ошибка загрузки заклинаний! ${uiState.message}",
                viewModel::onRetry
            )

            is UiState.Success -> SpellsListView(uiState.data, grades, onSelect)
        }
    }
}

@Composable
fun SpellsListView(
    spells: List<Spell>,
    grades: Map<String, Grade>,
    onSelect: (id: Int) -> Unit,
) {
    if (spells.isEmpty()) {
        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary),
        ) {
            Text(
                text = "Нет заклинаний",
                modifier = Modifier.padding(10.dp),
            )
        }

        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxHeight(),
        contentPadding = PaddingValues(5.dp),
        verticalArrangement = Arrangement.spacedBy(15.dp)
    ) {
        items(items = spells, key = { it.id }) { spell ->
            SpellItem(
                spell = spell,
                grade = grades[spell.name] ?: Grade.NO_GRADE,
                onClick = { onSelect(spell.id) }
            )
        }
    }
}
