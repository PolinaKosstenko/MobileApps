package com.example.harrypotterapi.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harrypotterapi.data.CharacterRepository
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import java.time.LocalDate
import kotlin.Int

data class HarryPotterAPIUIState(
    val query: String = "",
    val isLoading: Boolean = true,
    val errorMessage: String = "",
    val showFavourites: Boolean = false,
    val favourites: List<Int> = listOf(),
    val allowedHouses: List<House> = listOf(),
    val showWizards: Boolean = false,
)

@HiltViewModel
class HarryPotterAPIViewModel @Inject constructor(private val repository: CharacterRepository): ViewModel() {
    private var charactersDb by mutableStateOf(emptyList<Character>())
    var uiState: HarryPotterAPIUIState by mutableStateOf(HarryPotterAPIUIState())

    private var favouritesItems by mutableStateOf(emptyList<Character>())

    init {
        loadFavourites()
    }

    private fun loadFavourites() {
        viewModelScope.launch {
            try {
                val favs = repository.getFavourites()
                favouritesItems = favs
                uiState = uiState.copy(favourites = favs.map { it.id })
            } catch (ex: Exception) {
                uiState = uiState.copy(
                    errorMessage = "Что-то пошло не так"
                )
            }
        }
    }


    val characters: List<Character>
        get() {
            var characters: List<Character> = charactersDb

            if (uiState.showFavourites) {
                characters = characters.filter { c -> c.id in uiState.favourites }
            }

            if (!uiState.allowedHouses.isEmpty()) {
                characters = characters.filter { c -> c.house in uiState.allowedHouses }
            }

            if (uiState.showWizards) {
                characters = characters.filter { c -> c.wizard }
            }

            if (!uiState.query.isEmpty()) {
                characters = characters.filter {
                    c -> c.name.lowercase().contains(uiState.query.lowercase()) }
            }

            return characters.sortedBy { character -> character.id }
        }

    fun setLoading() {
        uiState = uiState.copy(
            isLoading = true,
            errorMessage = ""
        )
    }

    fun loadCharacters() {
        if (charactersDb.isNotEmpty()) return

        viewModelScope.launch {
            try {
                charactersDb = repository.getCharacters()
                uiState = uiState.copy(isLoading = false)
            } catch (ex: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = "Что-то пошло не так"
                )
            }
        }
    }

    fun getCharacter(id: Int): Character {
        return charactersDb[id];
    }

    fun onSearchChange(query: String) {
        uiState = uiState.copy(query = query)
        if (uiState.query == "") return;
    }

    fun onToggleFavouritesFilter() {
        uiState = uiState.copy(showFavourites = !uiState.showFavourites)
    }
//
    fun onToggleFavourite(id: Int) {

        viewModelScope.launch {
            val currentItems = favouritesItems
            val currentIds = uiState.favourites

            if (id in currentIds) {
                repository.removeFavourite(id)
                favouritesItems = currentItems.filterNot {it.id == id}
                uiState = uiState.copy(favourites = currentIds - id)
            } else {
                val character = charactersDb.firstOrNull() { it.id == id }
                if (character == null) {
                    uiState = uiState.copy(errorMessage = "Не удалось добавить в избранное")
                    return@launch
                }
                repository.addFavourite(character)
                favouritesItems = listOf(character) + currentItems
                uiState = uiState.copy(favourites = currentIds + id)
            }
        }

        uiState = uiState.copy(
            favourites =
                if (id in uiState.favourites) uiState.favourites - id
                else uiState.favourites + id)
    }

    fun isFavourite(id: Int): Boolean {
        return id in uiState.favourites
    }

    fun onToggleWizardFilter() {
        uiState = uiState.copy(showWizards = !uiState.showWizards)
    }

    fun onAllowHouse(house: House) {
        uiState = uiState.copy(allowedHouses =
            if (house in uiState.allowedHouses) uiState.allowedHouses - house
            else uiState.allowedHouses + house)
    }

}