package com.example.harrypotterapi.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@TypeConverters(Converters::class)
@Database (
    entities = [CharacterEntity::class, SpellEntity::class],
    version = 6,
    exportSchema = false
)
abstract class CharacterDatabase : RoomDatabase() {

    abstract fun characterDao(): CharacterDao
    abstract fun spellDao(): SpellDao
}
