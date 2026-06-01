package com.example.harrypotterapi.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.model.Spell
import java.time.LocalDate

@Entity(tableName = "spells")
data class SpellEntity (
    @PrimaryKey
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
)


fun SpellEntity.toDomain(): Spell =
    Spell(id, name, description, image)
