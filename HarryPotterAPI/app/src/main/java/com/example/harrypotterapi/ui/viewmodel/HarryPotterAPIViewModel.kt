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
import kotlin.collections.filter

data class FilterState(
    val query: String = "",
    val showFavourites: Boolean = false,
    val showWizards: Boolean = false,
    val allowedHouses: Set<House> = emptySet()
)

@HiltViewModel
class HarryPotterAPIViewModel @Inject constructor(
    private val repository: CharacterRepository
) : ViewModel() {
    private val searchQueryFlow = MutableStateFlow("")
    private val showFavouritesFilterFlow = MutableStateFlow(false)
    private val showWizardsFilterFlow = MutableStateFlow(false)
    private val allowedHousesFlow = MutableStateFlow<Set<House>>(emptySet())

    private val isLoadingFlow = MutableStateFlow(true)
    private val errorMessageFlow = MutableStateFlow<String?>(null)

    private val favouritesFlow = repository.observeFavourites()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val filterStateFlow = combine(
        searchQueryFlow,
        showFavouritesFilterFlow,
        showWizardsFilterFlow,
        allowedHousesFlow
    ) { query, showFavourites, showWizards, allowedHouses ->
        FilterState(query, showFavourites, showWizards, allowedHouses)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = FilterState()
    )

    private val retryEvents = MutableSharedFlow<Unit>(extraBufferCapacity = 1)
    val charactersFromApiFlow: StateFlow<List<Character>> = retryEvents
        .onStart { emit(Unit) }
        .flatMapLatest {
            flow {
                emit(repository.getCharacters())
            }.catch { e ->
                errorMessageFlow.value = "Ошибка загрузки: ${e.message}"
                emit(emptyList())
            }.onStart {
                isLoadingFlow.value = true
            }.onCompletion {
                isLoadingFlow.value = false
            }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = emptyList()
        )

    val characters: StateFlow<List<Character>> = combine(
        charactersFromApiFlow,
        filterStateFlow,
        favouritesFlow
    ) { allCharacters: List<Character>,
        filterState: FilterState,
        favourites: List<Character> ->
        var result = allCharacters

        if (filterState.showFavourites) {
            val favouriteIds = favourites.map { it.id }.toSet()
            result = result.filter { it.id in favouriteIds }
        }

        if (filterState.allowedHouses.isNotEmpty()) {
            result = result.filter { it.house in filterState.allowedHouses }
        }

        if (filterState.showWizards) {
            result = result.filter { it.wizard }
        }

        if (filterState.query.isNotBlank()) {
            val lowerQuery = filterState.query.lowercase()
            result = result.filter {
                it.name.lowercase().contains(lowerQuery)
            }
        }

        result.sortedBy { it.id }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = emptyList()
    );


    val isLoading: StateFlow<Boolean> = isLoadingFlow.asStateFlow()

    val errorMessage: StateFlow<String?> = errorMessageFlow.asStateFlow()

    fun onSearchChange(query: String) {
        searchQueryFlow.value = query
    }

    fun onToggleFavouritesFilter() {
        showFavouritesFilterFlow.update { !it }
    }

    fun onToggleWizardFilter() {
        showWizardsFilterFlow.update { !it }
    }

    fun onAllowHouse(house: House) {
        allowedHousesFlow.update { current ->
            if (house in current) current - house
            else current + house
        }
    }

    private fun getCharacterAndFavouriteStatus(id: Int): Pair<Character?, Boolean> {
        val character = charactersFromApiFlow.value.firstOrNull { it.id == id }
        val isFavourite = favouritesFlow.value.any { it.id == id }
        return character to isFavourite
    }

    fun isFavouriteFlow(characterId: Int): StateFlow<Boolean> {
        return favouritesFlow.map { favourites ->
            favourites.any { it.id == characterId }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = false
        )
    }

    fun onToggleFavourite(id: Int) {
        viewModelScope.launch {
            val (character, isFavourite) = getCharacterAndFavouriteStatus(id)
            if (character == null) return@launch

            if (isFavourite) {
                repository.removeFavourite(id)
            } else {
                repository.addFavourite(character)
            }
        }
    }

    fun onRetry() {
        viewModelScope.launch {
            errorMessageFlow.value = null
            retryEvents.tryEmit(Unit)
        }
    }
}
