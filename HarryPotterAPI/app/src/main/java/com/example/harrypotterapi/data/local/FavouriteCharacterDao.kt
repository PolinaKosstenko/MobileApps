package com.example.harrypotterapi.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface FavouriteCharacterDao {

    @Query("DELETE FROM favourite_character WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM favourite_character ORDER BY addedAt DESC")
    suspend fun getAll(): List<FavouriteCharacterEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: FavouriteCharacterEntity)

    @Query("SELECT EXISTS(SELECT 1 FROM favourite_character WHERE id = :id)")
    suspend fun isFavourite(id: Int): Boolean

    @Query("SELECT * FROM favourite_character ORDER BY addedAt DESC")
    fun observeAll(): Flow<List<FavouriteCharacterEntity>>
}