package com.example.harrypotterapi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import java.time.LocalDate

@Entity(tableName = "characters")
data class CharacterEntity (
    @PrimaryKey(autoGenerate = true)
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
    val addedAt: Long = System.currentTimeMillis(),
)


fun CharacterEntity.toDomain(): Character =
    Character(id, name, gender, dateOfBirth, eyeColour, hairColour,
        species, wizard, house, ancestry, wandWood, wandCore, patronus, isFavourite)
