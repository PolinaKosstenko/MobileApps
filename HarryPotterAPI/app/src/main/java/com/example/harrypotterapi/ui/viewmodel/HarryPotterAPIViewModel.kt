package com.example.harrypotterapi.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harrypotterapi.data.CharacterRepository
import com.example.harrypotterapi.data.SpellsRepository
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.model.Spell
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

sealed class UiState<out T> {
    data object Loading : UiState<Nothing>()
    data class Error(val message: String) : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
}

@HiltViewModel
class HarryPotterAPIViewModel @Inject constructor(
    private val repository: CharacterRepository,
    private val spellsRepository: SpellsRepository
) : ViewModel() {
    private val searchQueryFlow = MutableStateFlow("")
    private val showFavouritesFilterFlow = MutableStateFlow(false)
    private val showWizardsFilterFlow = MutableStateFlow(false)
    private val allowedHousesFlow = MutableStateFlow<Set<House>>(emptySet())

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

    val getCharactersUiState: StateFlow<UiState<List<Character>>> = retryEvents
        .onStart { emit(Unit) }
        .flatMapLatest {
            combine(
                repository.getCharacters(),
                filterStateFlow,
            ) { allCharacters, filterState ->
                applyFilters(allCharacters, filterState)
            }
                .map { characters -> UiState.Success(characters) as UiState<List<Character>> }
                .catch { e -> emit(UiState.Error(e.message ?: "Неизвестная ошибка")) }
                .onStart {
                    if (repository.isEmpty()) {
                        emit(UiState.Loading)
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    private val selectedCharacterId = MutableStateFlow<Int?>(null)

    fun selectCharacter(id: Int) {
        selectedCharacterId.value = id
    }

    val selectedCharacterUiState: StateFlow<UiState<Character>> = selectedCharacterId
        .filterNotNull()
        .flatMapLatest { id ->
            repository.getCharacter(id)
                .map { character ->
                    if (character == null) {
                        UiState.Error("Персонаж не найден") as UiState<Character>
                    } else {
                        UiState.Success(character)
                    }
                }
                .catch { e -> emit(UiState.Error(e.message ?: "Неизвестная ошибка")) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    val getSpellsUiState: StateFlow<UiState<List<Spell>>> = retryEvents
        .onStart { emit(Unit) }
        .flatMapLatest {
            spellsRepository.getSpells()
                .map { spells -> UiState.Success(spells) as UiState<List<Spell>> }
                .catch { e -> emit(UiState.Error(e.message ?: "Неизвестная ошибка")) }
                .onStart {
                    if (spellsRepository.isEmpty()) {
                        emit(UiState.Loading)
                    }
                }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )


    private fun applyFilters(
        allCharacters: List<Character>,
        filterState: FilterState,
    ): List<Character> {
        var result = allCharacters

        if (filterState.showFavourites) {
            result = result.filter { it.isFavourite }
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

        return result.sortedBy { it.id }
    }

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

    fun onToggleFavourite(id: Int) {
        viewModelScope.launch {
            repository.toggleFavourite(id)
        }
    }

    fun onRetry() {
        viewModelScope.launch {
            retryEvents.tryEmit(Unit)
        }
    }

    private val selectedSpellId = MutableStateFlow<Int?>(null)
    fun selectSpell(id: Int) {
        selectedSpellId.value = id
    }
    val selectedSpellUiState: StateFlow<UiState<Spell>> = selectedSpellId
        .filterNotNull()
        .flatMapLatest { id ->
            spellsRepository.getSpell(id)
                .map { spell ->
                    if (spell == null) {
                        UiState.Error("Заклинание не найдено") as UiState<Spell>
                    } else {
                        UiState.Success(spell)
                    }
                }
                .catch { e -> emit(UiState.Error(e.message ?: "Неизвестная ошибка")) }
        }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = UiState.Loading
        )

    fun deleteCharacter(id: Int) {
        viewModelScope.launch {
            repository.deleteCharacter(id)
        }
    }

}
