package com.example.harrypotterapi.ui.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIUIState
import com.example.harrypotterapi.ui.widget.CharacterItem

@Composable
fun CharacterListScreen(
    state: HarryPotterAPIUIState,
    characters: List<Character>,
    setLoading: () -> Unit,
    onSearchChange: (String) -> Unit,
    onToggleWizardFilter: () -> Unit,
    onToggleFavouritesFilter: () -> Unit,
    onAllowHouse: (House) -> Unit,
    onSelect: (Int) -> Unit
) {
    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (!state.errorMessage.isEmpty()) {
            Text(
                text = "Ошибка загрузки персонажей! ${state.errorMessage}",
                color = Color.Red
            )

            Button(
                modifier = Modifier.fillMaxWidth(),
                onClick = setLoading,
            ) {
                Text("Попробовать заново")
            }

            return
        }

        OutlinedTextField(
            value = state.query,
            onValueChange = onSearchChange,
            modifier = Modifier.fillMaxWidth(),
            label = { Text("Введите имя персонажа") },
            singleLine = true
        )

        if (state.isLoading) {
            Text(
                text = "Загрузка...",
            )

            return
        }

        Card(
            modifier = Modifier.fillMaxWidth().padding(8.dp),

            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.secondary),
        ) {
            FlowRow(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Column(
                    modifier = Modifier
                        .selectable(
                            selected = state.showWizards,
                            onClick = onToggleWizardFilter
                        )
                        .background(color =
                            if (state.showWizards) Color(255, 215, 0)
                            else Color.Gray)
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
                            selected = state.showFavourites,
                            onClick = onToggleFavouritesFilter
                        )
                        .background(color = if (state.showFavourites) Color.Magenta else Color.Gray)
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
                                selected = house in state.allowedHouses,
                                onClick = { onAllowHouse(house) }
                            )
                            .background(color = if (house in state.allowedHouses) when (house) {
                                House.Gryffindor -> Color.Red
                                House.Hufflepuff -> Color.Yellow
                                House.Ravenclaw -> Color.Blue
                                House.Slytherin -> Color.Green
                                else -> Color.Black
                            } else Color.Gray)
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
                            color = if (house in state.allowedHouses)when (house) {
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

        if (characters.isEmpty()) {
            Text(
                text = "Нет персонажей",
                modifier = Modifier.padding(start = 8.dp)
            )

            return
        }

        LazyColumn(
            modifier = Modifier.fillMaxHeight(),
            contentPadding = PaddingValues(5.dp),
            verticalArrangement = Arrangement.spacedBy(15.dp)) {
                items(items = characters, key = { it.id }) { character ->
                    CharacterItem(
                        character = character,
                        onClick = { onSelect(character.id) }
                    )
                }
            }
        }
    }
