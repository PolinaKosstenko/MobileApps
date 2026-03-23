package com.example.harrypotterapi.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface FavouriteCharacterDao {

    @Insert(onConflict = OnConflictStrategy.NONE)
    suspend fun insert(entity: FavouriteCharacterEntity)

    @Query("DELETE FROM favourite_character WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM favourite_character ORDER BY addedAt DESC")
    suspend fun getAll(): List<FavouriteCharacterEntity>
}