package com.example.harrypotterapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harrypotterapi.data.CharacterRepository
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

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
class HarryPotterAPIViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(HarryPotterAPIUIState())
    val uiState: StateFlow<HarryPotterAPIUIState> = _uiState.asStateFlow()

    private val charactersFlow = MutableStateFlow<List<Character>>(emptyList())

    private val favouriteFlow: StateFlow<List<Character>> =
        repository.observeFavourites()
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    init {
        loadCharacters()
        observeFavourites()
    }

    fun loadCharacters() {
        viewModelScope.launch {
            try {
                _uiState.update { it.copy(isLoading = true, errorMessage = "") }

                val characters = repository.getCharacters()
                charactersFlow.value = characters

                _uiState.update { it.copy(isLoading = false) }

            } catch (e: Exception) {
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        errorMessage = "Что-то пошло не так"
                    )
                }
            }
        }
    }

    private fun observeFavourites() {
        viewModelScope.launch {
            favouriteFlow.collect { favs ->
                _uiState.update {
                    it.copy(favourites = favs.map { c -> c.id })
                }
            }
        }
    }

    val characters: StateFlow<List<Character>> =
        combine(charactersFlow, uiState) { characters, state ->

            var result = characters

            if (state.showFavourites) {
                result = result.filter { it.id in state.favourites }
            }

            if (state.allowedHouses.isNotEmpty()) {
                result = result.filter { it.house in state.allowedHouses }
            }

            if (state.showWizards) {
                result = result.filter { it.wizard }
            }

            if (state.query.isNotEmpty()) {
                result = result.filter {
                    it.name.lowercase().contains(state.query.lowercase())
                }
            }

            result.sortedBy { it.id }
        }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5_000),
                initialValue = emptyList()
            )

    fun onSearchChange(query: String) {
        _uiState.update { it.copy(query = query) }
    }

    fun onToggleFavouritesFilter() {
        _uiState.update {
            it.copy(showFavourites = !it.showFavourites)
        }
    }

    fun onToggleWizardFilter() {
        _uiState.update {
            it.copy(showWizards = !it.showWizards)
        }
    }

    fun onAllowHouse(house: House) {
        _uiState.update {
            val current = it.allowedHouses
            it.copy(
                allowedHouses =
                    if (house in current) current - house
                    else current + house
            )
        }
    }

    fun onToggleFavourite(id: Int) {
        viewModelScope.launch {

            val character = charactersFlow.value.firstOrNull { it.id == id }
                ?: return@launch

            val isFav = id in _uiState.value.favourites

            if (isFav) {
                repository.removeFavourite(id)
            } else {
                repository.addFavourite(character)
            }
        }
    }

    fun isFavourite(id: Int): Boolean {
        return id in uiState.value.favourites
    }

    fun getCharacter(id: Int): Character? {
        return charactersFlow.value.firstOrNull { it.id == id }
    }
}