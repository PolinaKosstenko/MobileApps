package com.example.harrypotterapi.ui.viewmodel

import com.example.harrypotterapi.FakeCharactersRepository
import com.example.harrypotterapi.MainDispatcherRule
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*
import org.junit.Rule
import org.junit.Test
import java.time.LocalDate


class HarryPotterAPIViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val harry = Character (
        1,
        "Harry Potter",
        "male",
        LocalDate.of(1980, 7, 31),
        "green",
        "black",
        "human",
        true,
        House.Gryffindor,
        "holly",
        "phoenix tail feather",
        "stag",
        )

    private val severus = Character (
        2,
        "Severus Snape",
        "male",
        LocalDate.of(1960, 1, 9),
        "black",
        "black",
        "human",
        true,
        House.Slytherin,
        "No wand wood",
        "No wand core",
        "doe",
    )


    @Test
    fun `initial state should be correct`() = runTest {
        val repository = FakeCharactersRepository()
        val viewModel = HarryPotterAPIViewModel(repository)
        advanceUntilIdle()

        with(viewModel.uiState) {
            assertEquals("", query)
            assertEquals(true, isLoading)
            assertEquals("", errorMessage)
            assertEquals(false, showFavourites)
            assertEquals(emptyList<Int>(), favourites)
            assertEquals(emptyList<House>(), allowedHouses)
            assertEquals(false, showWizards)
        }
    }

    @Test
    fun `onSearchChange update query and clear error`() = runTest {
        val repository = FakeCharactersRepository()
        val viewModel = HarryPotterAPIViewModel(repository)
        advanceUntilIdle()


        viewModel.onSearchChange("harry")

        assertEquals("harry", viewModel.uiState.query)

    }

    @Test
    fun `loadCharacters should load data successfully`() = runTest {
        val repository = FakeCharactersRepository()

        val searchResultField = FakeCharactersRepository::class.java.getDeclaredField("searchResult")
        searchResultField.isAccessible = true
        searchResultField.set(repository, listOf(harry, severus))

        val viewModel = HarryPotterAPIViewModel(repository)
        viewModel.setLoading()
        viewModel.loadCharacters()
        advanceUntilIdle()

        assertEquals(false, viewModel.uiState.isLoading)
        assertEquals("", viewModel.uiState.errorMessage)
        assertEquals(2, viewModel.characters.size)
        assertTrue(viewModel.characters.contains(harry))
        assertTrue(viewModel.characters.contains(severus))

    }

    @Test
    fun `loadCharacters should handle error`() = runTest {
        val repository = FakeCharactersRepository()
        repository.failSearch = true

        val viewModel = HarryPotterAPIViewModel(repository)
        viewModel.setLoading()
        viewModel.loadCharacters()
        advanceUntilIdle()

        assertEquals(false, viewModel.uiState.isLoading)
        assertEquals("Что-то пошло не так", viewModel.uiState.errorMessage)
        assertEquals(emptyList<Character>(), viewModel.characters)
    }

    @Test
    fun `characters should filter by house correctly`() = runTest {
        val repository = FakeCharactersRepository()
        val searchResultField = FakeCharactersRepository::class.java.getDeclaredField("searchResult")
        searchResultField.isAccessible = true
        searchResultField.set(repository, listOf(harry, severus))

        val viewModel = HarryPotterAPIViewModel(repository)
        viewModel.loadCharacters()
        advanceUntilIdle()

        viewModel.onAllowHouse(House.Gryffindor)

        val filteredCharacters = viewModel.characters
        assertEquals(1, filteredCharacters.size)
        assertTrue(filteredCharacters.all { it.house == House.Gryffindor })
        assertTrue(filteredCharacters.contains(harry))

    }

    @Test
    fun `characters should return empty list when no matches`() = runTest {
        val repository = FakeCharactersRepository()
        val searchResultField = FakeCharactersRepository::class.java.getDeclaredField("searchResult")
        searchResultField.isAccessible = true
        searchResultField.set(repository, listOf(harry, severus))

        val viewModel = HarryPotterAPIViewModel(repository)
        viewModel.loadCharacters()
        advanceUntilIdle()


        viewModel.onSearchChange("NonExistentName")

        assertTrue(viewModel.characters.isEmpty())
    }

    @Test
    fun `loadCharacters should retry after error`() = runTest {

        val repository = FakeCharactersRepository()
        val searchResultField = FakeCharactersRepository::class.java.getDeclaredField("searchResult")
        searchResultField.isAccessible = true
        searchResultField.set(repository, listOf(harry, severus))

        val viewModel = HarryPotterAPIViewModel(repository)


        repository.failSearch = true


        viewModel.setLoading()
        viewModel.loadCharacters()
        advanceUntilIdle()


        assertEquals(false, viewModel.uiState.isLoading)
        assertEquals("Что-то пошло не так", viewModel.uiState.errorMessage)
        assertEquals(emptyList<Character>(), viewModel.characters)


        repository.failSearch = false

        viewModel.setLoading()
        viewModel.loadCharacters()
        advanceUntilIdle()


        assertEquals(false, viewModel.uiState.isLoading)
        assertEquals("", viewModel.uiState.errorMessage)
        assertEquals(2, viewModel.characters.size)
        assertTrue(viewModel.characters.contains(harry))
        assertTrue(viewModel.characters.contains(severus))

    }


}