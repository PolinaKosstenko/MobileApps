package com.example.harrypotterapi.model

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
    val wandWood: String,
    val wandCore: String,
    val patronus: String,
)
