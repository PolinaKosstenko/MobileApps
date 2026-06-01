package com.example.harrypotterapi.model

import com.example.harrypotterapi.data.local.CharacterEntity
import java.time.LocalDate

enum class House {
    Gryffindor,
    Hufflepuff,
    Ravenclaw,
    Slytherin,
    NoHouse
}

data class Character(
    val id: Int,
    val name: String,
    val gender: String,
    val dateOfBirth: LocalDate?,
    val eyeColour: String,
    val hairColour: String,
    val species: String,
    val wizard: Boolean,
    val house: House?,
    val ancestry: String,
    val wandWood: String,
    val wandCore: String,
    val patronus: String,
    val isFavourite: Boolean,
)

fun Character.toEntity(): CharacterEntity =
    CharacterEntity(id, name, gender, dateOfBirth, eyeColour, hairColour,
        species, wizard, house, ancestry, wandWood, wandCore, patronus, isFavourite)

fun Character.validateName(): String = when {
    name.isBlank() -> "Введите имя"
    name.length < 2 -> "Имя не должно быть короче двух символов"
    else -> ""
}

fun validateField(value: String): String = when {
    value.isBlank() -> "Выберите вариант"
    else -> ""
}

fun Character.validateGender(): String = validateField(gender)
fun Character.validateEyeColour(): String = validateField(eyeColour)
fun Character.validateHairColour(): String = validateField(hairColour)
fun Character.validateAncestry(): String = validateField(ancestry)
fun Character.validateWandWood(): String = validateField(wandWood)
fun Character.validateWandCore(): String = validateField(wandCore)
fun Character.validatePatronus(): String = validateField(patronus)
