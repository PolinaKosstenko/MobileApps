package com.example.harrypotterapi.ui.screen


import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIViewModel
import com.example.harrypotterapi.ui.viewmodel.UiState
import com.example.harrypotterapi.ui.widget.ErrorView
import com.example.harrypotterapi.ui.widget.LoadingView


@Composable
fun CharacterDetailScreen(
    viewModel: HarryPotterAPIViewModel,
    characterId: Int,
    onToggleFavourite: (id: Int) -> Unit,
    onLoadList: () -> Unit
) {
    LaunchedEffect(characterId) {
        viewModel.selectCharacter(characterId)
    }

    val state by viewModel.selectedCharacterUiState.collectAsStateWithLifecycle()

    when (val uiState = state) {
        is UiState.Loading -> LoadingView()
        is UiState.Error -> ErrorView(
            "Ошибка загрузки персонажа! ${uiState.message}",
            viewModel::onRetry
        )
        is UiState.Success -> CharacterView(uiState.data, onToggleFavourite, onLoadList)
    }
}

@Composable
fun CharacterView(
    character: Character,
    onToggleFavourite: (id: Int) -> Unit,
    onLoadList: () -> Unit
) {

    val characterKeyToText = mapOf(
        "Имя" to character.name,
        "Пол" to character.gender,
        "Дата рождения" to character.dateOfBirth.toString(),
        "Цвет глаз" to character.eyeColour,
        "Цвет волос" to character.hairColour,
        "Раса" to character.species,
        "Волшебник" to if (character.wizard) "Да" else "Нет",
        "Факультет" to "${character.house}",
        "Дерево палочки" to character.wandWood,
        "Сердцевина палочки" to character.wandCore,
        "Патронус" to character.patronus
    )

    Card(
        modifier = Modifier.padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondary),
    ) {
        for ((field, value) in characterKeyToText) {
            Row(
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                Text(
                    modifier = Modifier.weight(1f),
                    text = field,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Start,
                )

                Text(
                    text = value,
                    fontSize = 20.sp,
                    textAlign = TextAlign.End,
                )
            }
        }

        Button(
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            ),
            onClick = { onToggleFavourite(character.id) }
        ) {
            Text(if (character.isFavourite) "❤\uFE0F" else "Добавить в любимых")
        }

        Spacer(modifier = Modifier.padding(4.dp))

        Button(
            modifier = Modifier.fillMaxWidth(),
            onClick = onLoadList,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Text("Назад")
        }
    }
}
