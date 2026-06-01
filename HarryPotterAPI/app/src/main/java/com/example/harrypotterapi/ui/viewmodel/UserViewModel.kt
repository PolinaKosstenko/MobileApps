package com.example.harrypotterapi.ui.viewmodel

import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.harrypotterapi.data.CharacterRepository
import com.example.harrypotterapi.data.SpellsRepository
import com.example.harrypotterapi.data.UserRepository
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.Grade
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.model.Spell
import com.example.harrypotterapi.model.toEntity
import com.example.harrypotterapi.model.validateAncestry
import com.example.harrypotterapi.model.validateEyeColour
import com.example.harrypotterapi.model.validateField
import com.example.harrypotterapi.model.validateGender
import com.example.harrypotterapi.model.validateHairColour
import com.example.harrypotterapi.model.validateName
import com.example.harrypotterapi.model.validatePatronus
import com.example.harrypotterapi.model.validateWandCore
import com.example.harrypotterapi.model.validateWandWood
import com.example.quiz.ui.theme.ThemeStyle
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import javax.inject.Inject
import kotlin.collections.filter

enum class FieldError {
    GENDER,
    HAIR_COLOR,
    EYE_COLOR,
    ANCESTRY,
    WAND_WOOD,
    WAND_CORE,
    PATRONUS
}

@HiltViewModel
class UserViewModel @Inject constructor(
    private val repository: CharacterRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    private val userCharacterFlow = MutableStateFlow(
        Character(
            Int.MIN_VALUE,
            "",
            "",
            LocalDate.now(),
            "",
            "",
            "human",
            true,
            null,
            "",
            "",
            "",
            "",
            false
        )
    )

    val userLocation: StateFlow<House> = userRepository.getLocation()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = House.NoHouse
        )

    private val isLoadingFlow = MutableStateFlow(true)
    val isLoading: StateFlow<Boolean> = isLoadingFlow.asStateFlow()

    val userCharacter: StateFlow<Character> = userRepository.getCharacter()
        .distinctUntilChanged()
        .flatMapLatest { savedId ->
            if (savedId == Int.MIN_VALUE) {
                isLoadingFlow.value = false
                userCharacterFlow
            } else {
                repository.getCharacter(savedId)
                    .onEach { isLoadingFlow.value = false }
            }
        }
        .filterNotNull()
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5_000),
            initialValue = userCharacterFlow.value
        )


    private val nameErrorMessageFlow = MutableStateFlow("")
    val nameErrorMessage: StateFlow<String> = nameErrorMessageFlow.asStateFlow()
    private val fieldErrorMessageFlow = MutableStateFlow("")
    val fieldErrorMessage: StateFlow<String> = fieldErrorMessageFlow.asStateFlow()
    private val fieldErrorActiveFlow = MutableStateFlow(
        MutableList(FieldError.entries.size) { false }
    )
    val fieldErrorActive: StateFlow<List<Boolean>> = fieldErrorActiveFlow.asStateFlow()

    fun onNameChange(name: String) {
        userCharacterFlow.value = userCharacterFlow.value.copy(name = name)
        nameErrorMessageFlow.value = userCharacterFlow.value.validateName()
    }

    fun onGenderChange(gender: String) {
        userCharacterFlow.update { userCharacter -> userCharacter.copy(gender = gender) }
        fieldErrorMessageFlow.update { userCharacterFlow.value.validateGender() }

        fieldErrorActiveFlow.update { fieldErrorActive ->
            fieldErrorActive.apply {
                this[FieldError.GENDER.ordinal] = !fieldErrorMessageFlow.value.isEmpty()
            }
        }
    }

    fun onDateOfBirthChange(dateOfBirth: Long?) {
        dateOfBirth?.let {
            val localDate = LocalDate.ofInstant(
                Instant.ofEpochMilli(dateOfBirth),
                ZoneId.systemDefault()
            )
            userCharacterFlow.value = userCharacterFlow.value.copy(dateOfBirth = localDate)
        }
    }

    fun onHairColorChange(hairColour: String) {
        userCharacterFlow.value = userCharacterFlow.value.copy(hairColour = hairColour)
        fieldErrorMessageFlow.value = userCharacterFlow.value.validateHairColour()

        fieldErrorActiveFlow.update { fieldErrorActive ->
            fieldErrorActive.apply {
                this[FieldError.HAIR_COLOR.ordinal] = !fieldErrorMessageFlow.value.isEmpty()
            }
        }
    }

    fun onEyeColorChange(eyeColour: String) {
        userCharacterFlow.value = userCharacterFlow.value.copy(eyeColour = eyeColour)
        fieldErrorMessageFlow.value = userCharacterFlow.value.validateEyeColour()

        fieldErrorActiveFlow.update { fieldErrorActive ->
            fieldErrorActive.apply {
                this[FieldError.EYE_COLOR.ordinal] = !fieldErrorMessageFlow.value.isEmpty()
            }
        }
    }

    fun onAncestryChange(ancestry: String) {
        userCharacterFlow.value = userCharacterFlow.value.copy(ancestry = ancestry)
        fieldErrorMessageFlow.value = userCharacterFlow.value.validateAncestry()

        fieldErrorActiveFlow.update { fieldErrorActive ->
            fieldErrorActive.apply {
                this[FieldError.ANCESTRY.ordinal] = !fieldErrorMessageFlow.value.isEmpty()
            }
        }
    }

    fun onWandWoodChange(wandWood: String) {
        userCharacterFlow.value = userCharacterFlow.value.copy(wandWood = wandWood)
        fieldErrorMessageFlow.value = userCharacterFlow.value.validateWandWood()

        fieldErrorActiveFlow.update { fieldErrorActive ->
            fieldErrorActive.apply {
                this[FieldError.WAND_WOOD.ordinal] = !fieldErrorMessageFlow.value.isEmpty()
            }
        }
    }

    fun onWandCoreChange(wandCore: String) {
        userCharacterFlow.value = userCharacterFlow.value.copy(wandCore = wandCore)
        fieldErrorMessageFlow.value = userCharacterFlow.value.validateWandCore()

        fieldErrorActiveFlow.update { fieldErrorActive ->
            fieldErrorActive.apply {
                this[FieldError.WAND_CORE.ordinal] = !fieldErrorMessageFlow.value.isEmpty()
            }
        }
    }

    fun onPatronusChange(patronus: String) {
        userCharacterFlow.value = userCharacterFlow.value.copy(patronus = patronus)
        fieldErrorMessageFlow.value = userCharacterFlow.value.validatePatronus()

        fieldErrorActiveFlow.update { fieldErrorActive ->
            fieldErrorActive.apply {
                this[FieldError.PATRONUS.ordinal] = !fieldErrorMessageFlow.value.isEmpty()
            }
        }
    }

    fun onCreate(callback: () -> Unit) {
        nameErrorMessageFlow.value = userCharacterFlow.value.validateName()

        fieldErrorActiveFlow.update { fieldErrorActive ->
            fieldErrorActive.apply {
                this[FieldError.GENDER.ordinal] =
                    !userCharacterFlow.value.validateGender().isEmpty()
                this[FieldError.HAIR_COLOR.ordinal] =
                    !userCharacterFlow.value.validateHairColour().isEmpty()
                this[FieldError.EYE_COLOR.ordinal] =
                    !userCharacterFlow.value.validateEyeColour().isEmpty()
                this[FieldError.ANCESTRY.ordinal] =
                    !userCharacterFlow.value.validateAncestry().isEmpty()
                this[FieldError.WAND_WOOD.ordinal] =
                    !userCharacterFlow.value.validateWandWood().isEmpty()
                this[FieldError.WAND_CORE.ordinal] =
                    !userCharacterFlow.value.validateWandCore().isEmpty()
                this[FieldError.PATRONUS.ordinal] =
                    !userCharacterFlow.value.validatePatronus().isEmpty()
            }
        }

        fieldErrorMessageFlow.update { errorMessage ->
            if (fieldErrorActiveFlow.value.firstOrNull { it } == true) validateField("") else ""
        }


        viewModelScope.launch {
            if (
                nameErrorMessageFlow.value.isEmpty()
                && fieldErrorMessageFlow.value.isEmpty()
            ) {
                val id = repository.addCharacter(userCharacterFlow.value)
                userCharacterFlow.update { it.copy(id = id.toInt()) }

                userRepository.setCharacter(id.toInt())
                callback()
            }
        }
    }

    fun onHouseSorted(house: House) {
        viewModelScope.launch {
            repository.updateCharacterHouse(userCharacterFlow.value.id, house)
            userCharacterFlow.update { userCharacter -> userCharacter.copy(house = house) }
            setUserSelectedTheme(when (house) {
                House.Gryffindor -> ThemeStyle.GRYFFINDOR
                House.Slytherin -> ThemeStyle.SLYTHERIN
                House.Ravenclaw -> ThemeStyle.RAVENCLAW
                else -> ThemeStyle.HUFFLEPUFF
            })
            onSetLocation(house)
        }
    }

    fun onSetLocation(house: House) {
        viewModelScope.launch {
            userRepository.setLocation(house)
        }
    }

    fun getUserCharacter(): Flow<Int> = userRepository.getCharacter()
    fun getUserLocation(): Flow<House> = userRepository.getLocation()
    fun getUserGrades(): Flow<Map<String, Grade>> = userRepository.getGrades()
    fun getUserSelectedTheme(): Flow<ThemeStyle> = userRepository.getSelectedTheme()
    fun resetUserGrades() = viewModelScope.launch { userRepository.resetGrades() }
    fun resetUserCharacter() = viewModelScope.launch {
        userRepository.resetCharacter()
        userCharacterFlow.update {
            Character(
                Int.MIN_VALUE,
                "",
                "",
                LocalDate.now(),
                "",
                "",
                "human",
                true,
                null,
                "",
                "",
                "",
                "",
                false
            )
        }
    }

    fun setUserSelectedTheme(theme: ThemeStyle) {
        viewModelScope.launch {
            userRepository.setSelectedTheme(theme)
        }
    }

    fun onDrawingComplete(spellName: String, deviation: Float) {
        viewModelScope.launch {
            val grade = when {
                deviation in 0.0f..<2.0f -> Grade.OUTSTANDING
                deviation in 2.0f..<5.0f -> Grade.EXCEEDS_EXPECTATIONS
                deviation in 5.0f..<10.0f -> Grade.ACCEPTABLE
                deviation in 10.0f..<25.0f -> Grade.POOR
                deviation in 25.0f..<50.0f -> Grade.DREADFUL
                else -> Grade.TROLL
            }

            userRepository.setGrade(spellName, grade)
        }
    }
}
