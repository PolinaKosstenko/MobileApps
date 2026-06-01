package com.example.harrypotterapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface SpellDao {

    @Query("SELECT * FROM spells ORDER BY CASE WHEN image IS NULL THEN 1 ELSE 0 END ASC")
    fun getAllFlow(): Flow<List<SpellEntity>>

    @Query("SELECT * FROM spells WHERE id = :id")
    fun getSpell(id: Int): Flow<SpellEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addSpells(spells: List<SpellEntity>)
}
