package com.example.harrypotterapi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import java.time.LocalDate

@Entity(tableName = "favourite_character")
data class FavouriteCharacterEntity (
    @PrimaryKey
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
    val addedAt: Long = System.currentTimeMillis(),
)


fun FavouriteCharacterEntity.toDomain(): Character =
    Character(id, name, gender, dateOfBirth, eyeColour, hairColour,
        species, wizard, house, wandWood, wandCore, patronus)