package com.example.harrypotterapi.model

import com.example.harrypotterapi.data.local.SpellEntity

data class Spell(
    val id: Int,
    val name: String,
    val description: String,
    val image: String?,
)

fun Spell.toEntity(): SpellEntity =
    SpellEntity(id, name, description, image)
