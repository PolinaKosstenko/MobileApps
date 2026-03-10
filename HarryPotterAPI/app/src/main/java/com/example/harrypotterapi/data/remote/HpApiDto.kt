package com.example.harrypotterapi.data.remote

import com.google.gson.annotations.SerializedName
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.Int

data class HpApiDto(
    val id: String,
    val name: String,
    val species: String,
    val gender: String,
    val house: String,
    val dateOfBirth: String?,
    val wizard: Boolean,
    val eyeColour: String,
    val hairColour: String,
    val wand: HpApiWandDto,
    val patronus: String,
)

data class HpApiWandDto(
    val wood: String,
    val core: String
)



//{
//    "id": "9e3f7ce4-b9a7-4244-b709-dae5c1f1d4a8",
//    "name": "Harry Potter",
//    "species": "human",
//    "gender": "male",
//    "house": "Gryffindor",
//    "dateOfBirth": "31-07-1980",
//    "wizard": true,
//    "eyeColour": "green",
//    "hairColour": "black",
//    "wand": {
//        "wood": "holly",
//        "core": "phoenix tail feather",
//    },
//    "patronus": "stag"
//}

fun HpApiDto.dtoToCharacter(i: Int): Character? {
    val mapToHouse = mapOf<String, House>(
        "Gryffindor" to House.Gryffindor,
        "Hufflepuff" to House.Hufflepuff,
        "Ravenclaw" to House.Ravenclaw,
        "Slytherin" to House.Slytherin
    )

    return Character(
        id = i,
        name = if (name == "") "Noname" else name,
        gender = if (gender == "") "No gender" else gender,
        dateOfBirth = if (dateOfBirth == null || dateOfBirth == "") null else LocalDate
            .parse(dateOfBirth, DateTimeFormatter.ofPattern("d-M-yyyy")),
        eyeColour = if (eyeColour == "") "No eyeColor" else eyeColour,
        hairColour = if (hairColour == "") "No hairColor" else hairColour,
        species = if (species == "") "No species" else species,
        wizard = wizard,
        house = if (house == "") House.NoHouse else mapToHouse[house],
        wandWood = if (wand.wood == "") "No wand wood" else wand.wood,
        wandCore = if (wand.core == "") "No wand core" else wand.core,
        patronus = if (patronus == "") "No patronus" else patronus
    )
}


