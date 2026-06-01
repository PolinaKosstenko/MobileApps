package com.example.harrypotterapi.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.ui.viewmodel.FilterState
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIViewModel
import com.example.harrypotterapi.ui.viewmodel.UiState
import com.example.harrypotterapi.ui.widget.CharacterItem
import com.example.harrypotterapi.ui.widget.ErrorView
import com.example.harrypotterapi.ui.widget.LoadingView

@Composable
fun CharacterListScreen(
    viewModel: HarryPotterAPIViewModel,
    filters: FilterState,
    onSetLocation: (House) -> Unit,
    onSelect: (Int) -> Unit,
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        var room = "Общий зал"
        if (filters.allowedHouses.size == 1 && filters.allowedHouses.first() != House.NoHouse ) {
            room = "Гостинная ${
                when (filters.allowedHouses.first()) {
                    House.Gryffindor -> "Гриффиндора"
                    House.Slytherin -> "Слизерина"
                    House.Ravenclaw -> "Когтеврана"
                    else -> "Пуффендуя"
                }
            }"
        }

        onSetLocation(when (room) {
            "Гостинная Гриффиндора" -> House.Gryffindor
            "Гостинная Слизерина" -> House.Slytherin
            "Гостинная Когтеврана" -> House.Ravenclaw
            "Гостинная Пуффендуя" -> House.Hufflepuff
            else ->  House.NoHouse
        })


        Card(
            modifier = Modifier.padding(8.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary),
        ) {
            Text(
                text = room,
                modifier = Modifier.fillMaxWidth().padding(10.dp),
                textAlign = TextAlign.Center,
                fontSize = 24.sp
            )
        }


        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = filters.query,
                onValueChange = viewModel::onSearchChange,
                modifier = Modifier
                    .fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = MaterialTheme.colorScheme.secondary,
                    unfocusedContainerColor = MaterialTheme.colorScheme.secondary,
                    focusedTextColor = Color.White,
                    unfocusedTextColor = Color.White,
                    disabledTextColor = Color.White,
                    focusedLabelColor = Color.White,
                    unfocusedLabelColor = Color.White,
                    focusedPlaceholderColor = Color.White,
                    unfocusedPlaceholderColor = Color.White,
                    cursorColor = Color.White,
                ),
                label = { Text("Введите имя персонажа") },
                singleLine = true
            )

            Card(
                modifier = Modifier.fillMaxWidth().padding(8.dp),

                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.secondary
                ),
            ) {
                FlowRow(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .selectable(
                                selected = filters.showWizards,
                                onClick = viewModel::onToggleWizardFilter
                            )
                            .background(
                                color =
                                    if (filters.showWizards) Color(255, 215, 0)
                                    else Color.Gray
                            )
                            .padding(6.dp)
                    ) {
                        Text(
                            text = "Волшебники",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    Column(
                        modifier = Modifier
                            .selectable(
                                selected = filters.showFavourites,
                                onClick = viewModel::onToggleFavouritesFilter
                            )
                            .background(color = if (filters.showFavourites) Color.Magenta else Color.Gray)
                            .padding(6.dp)
                    ) {
                        Text(
                            text = "Любимые ❤\uFE0F",
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    for (house in House.entries) {
                        Column(
                            modifier = Modifier
                                .selectable(
                                    selected = house in filters.allowedHouses,
                                    onClick = { viewModel.onAllowHouse(house) }
                                )
                                .background(
                                    color = if (house in filters.allowedHouses) when (house) {
                                        House.Gryffindor -> Color.Red
                                        House.Hufflepuff -> Color.Yellow
                                        House.Ravenclaw -> Color.Blue
                                        House.Slytherin -> Color.Green
                                        else -> Color.Black
                                    } else Color.Gray
                                )
                                .padding(6.dp)
                        ) {
                            Text(
                                text = when (house) {
                                    House.Gryffindor -> "Gryffindor"
                                    House.Hufflepuff -> "Hufflepuff"
                                    House.Ravenclaw -> "Ravenclaw"
                                    House.Slytherin -> "Slytherin"
                                    else -> "No House"
                                },
                                color = if (house in filters.allowedHouses) when (house) {
                                    House.Gryffindor -> Color.White
                                    House.Ravenclaw -> Color.White
                                    House.NoHouse -> Color.White
                                    else -> Color.Black
                                } else Color.White
                            )
                        }
                    }
                }
            }

            val state by viewModel.getCharactersUiState.collectAsStateWithLifecycle()

            when (val uiState = state) {
                is UiState.Loading -> LoadingView()
                is UiState.Error -> ErrorView(
                    "Ошибка загрузки персонажей! ${uiState.message}",
                    viewModel::onRetry
                )

                is UiState.Success -> CharacterListView(uiState.data, onSelect)
            }
        }
    }
}

@Composable
fun CharacterListView(
    characters: List<Character>,
    onSelect: (Int) -> Unit
) {
    if (characters.isEmpty()) {
        Card(
            modifier = Modifier.padding(8.dp),
        ) {
            Text(
                text = "Нет персонажей",
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
        items(items = characters, key = { it.id }) { character ->
            CharacterItem(
                character = character,
                onClick = { onSelect(character.id) }
            )
        }
    }
}
