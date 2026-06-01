package com.example.harrypotterapi.data.local

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Entity
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CharacterDao {

    @Query("SELECT * FROM characters ORDER BY addedAt DESC")
    suspend fun getAll(): List<CharacterEntity>

    @Query("SELECT * FROM characters ORDER BY addedAt DESC")
    fun getAllFlow(): Flow<List<CharacterEntity>>

    @Query("SELECT * FROM characters WHERE id = :id")
    fun getCharacter(id: Int): Flow<CharacterEntity?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacter(entity: CharacterEntity): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addCharacters(entity: List<CharacterEntity>)

    @Update
    suspend fun updateCharacter(character: CharacterEntity)

    @Delete
    suspend fun deleteCharacter(character: CharacterEntity)

    @Query("SELECT isFavourite FROM characters WHERE id = :id LIMIT 1")
    fun isFavourite(id: Int): Flow<Boolean>

    @Query("SELECT * FROM characters WHERE isFavourite = 1 ORDER BY addedAt DESC")
    fun observeFavourites(): Flow<List<CharacterEntity>>
}
