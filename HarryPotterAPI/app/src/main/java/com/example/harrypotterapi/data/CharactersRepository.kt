package com.example.harrypotterapi.data

import com.example.harrypotterapi.data.local.CharacterDao
import com.example.harrypotterapi.data.local.CharacterEntity
import com.example.harrypotterapi.data.local.toDomain
import com.example.harrypotterapi.data.remote.HpApi
import com.example.harrypotterapi.data.remote.dtoToCharacter
import com.example.harrypotterapi.model.Character
import com.example.harrypotterapi.model.House
import com.example.harrypotterapi.model.toEntity
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.withContext

interface CharacterRepository {
    fun getCharacters(): Flow<List<Character>>
    fun getCharacter(id: Int): Flow<Character?>
    suspend fun addCharacter(character: Character): Long
    suspend fun updateCharacterHouse(id: Int, house: House)

    suspend fun toggleFavourite(id: Int)
    fun isFavourite(id: Int): Flow<Boolean>

    fun observeFavourites(): Flow<List<Character>>

    suspend fun isEmpty(): Boolean

    suspend fun deleteCharacter(id: Int)
}

class CharacterRepositoryImpl @Inject constructor(
    private val api: HpApi,
    private val characterDao: CharacterDao,
) : CharacterRepository {

    override fun getCharacters(): Flow<List<Character>> =
        characterDao.getAllFlow()
            .map { list -> list.map { it.toDomain() } }
            .onStart {
                val hasApiCharacters = characterDao.getAll().any { it.id > 0 }
                if (!hasApiCharacters) {
                    val apiCharacters = api.characters()
                    characterDao.addCharacters(
                        apiCharacters.mapIndexedNotNull { i, v ->
                            v.dtoToCharacter(i)?.toEntity()
                        }
                    )
                }
            }
            .flowOn(Dispatchers.IO)


    override fun getCharacter(id: Int): Flow<Character?> =
        characterDao.getCharacter(id)
            .filterNotNull()
            .map { it.toDomain() }

    override suspend fun addCharacter(character: Character): Long =
        withContext(Dispatchers.IO) {
            characterDao.addCharacter(character.toEntity())
        }

    override suspend fun updateCharacterHouse(id: Int, house: House) =
        withContext(Dispatchers.IO) {
            val character = characterDao.getCharacter(id).firstOrNull()
            character?.let {
                characterDao.updateCharacter(it.copy(house = house))
            } ?: Unit
        }

    override suspend fun toggleFavourite(id: Int) =
        withContext(Dispatchers.IO) {
            val character = characterDao.getCharacter(id).firstOrNull()
            character?.let {
                characterDao.updateCharacter(it.copy(isFavourite = !it.isFavourite))
            } ?: Unit
        }

    override fun isFavourite(id: Int): Flow<Boolean> =
        characterDao.isFavourite(id)
            .flowOn(Dispatchers.IO)

    override fun observeFavourites(): Flow<List<Character>> =
        characterDao.observeFavourites()
            .map { list -> list.map { it.toDomain() } }
            .flowOn(Dispatchers.IO)

    override suspend fun isEmpty(): Boolean =
        withContext(Dispatchers.IO) {
            val characters = characterDao.getAll()
            characters.isEmpty() || characters.all { it.id <= 0 }
        }

    override suspend fun deleteCharacter(id: Int) =
        withContext(Dispatchers.IO) {
            characterDao.getCharacter(id)
                .firstOrNull()
                ?.let { characterDao.deleteCharacter(it) }
                ?: Unit
        }
}
