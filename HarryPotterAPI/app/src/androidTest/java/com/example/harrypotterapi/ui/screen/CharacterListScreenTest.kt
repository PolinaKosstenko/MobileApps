package com.example.harrypotterapi.ui.screen

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.ui.viewmodel.HarryPotterAPIUIState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class CharacterListScreenTest {

    @get:Rule
    val composeRule = createComposeRule()

    private val testCharacter = Character(
        id = 1,
        name = "Harry Potter",
        gender = "male",
        dateOfBirth = LocalDate.of(1980, 7, 31),
        eyeColour = "green",
        hairColour = "black",
        species = "human",
        wizard = true,
        house = House.Gryffindor,
        wandWood = "holly",
        wandCore = "phoenix tail feather",
        patronus = "stag"
    )

    @Test
    fun clickOnCharacter_shouldOpenDetails() {

        var selectedCharacterId: Int? = null


        val state = HarryPotterAPIUIState(
            query = "",
            isLoading = false,
            errorMessage = "",
            showFavourites = false,
            favourites = emptyList(),
            allowedHouses = emptyList(),
            showWizards = false
        )


        composeRule.setContent {
            CharacterListScreen(
                state = state,
                characters = listOf(testCharacter),
                setLoading = {},
                onSearchChange = {},
                onToggleWizardFilter = {},
                onToggleFavouritesFilter = {},
                onAllowHouse = {},
                onSelect = { id ->
                    selectedCharacterId = id
                }
            )
        }


        composeRule.onNodeWithText("Harry Potter")
            .assertExists()
            .assertIsDisplayed()


        composeRule.onNodeWithText("Harry Potter")
            .performClick()


        assert(selectedCharacterId == 1) { "Ожидался ID=1, но получен $selectedCharacterId" }
    }
}