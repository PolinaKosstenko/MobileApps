package com.example.harrypotterapi.data

import com.example.harrypotterapi.data.local.FavouriteCharacterDao
import com.example.harrypotterapi.data.local.FavouriteCharacterEntity
import com.example.harrypotterapi.data.local.toDomain
import com.example.harrypotterapi.data.remote.HpApi
import com.example.harrypotterapi.data.remote.dtoToCharacter
import com.example.harrypotterapi.model.Character
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

interface CharacterRepository {
    suspend fun getCharacters(): List<Character>

    suspend fun addFavourite(character: Character)
    suspend fun removeFavourite(id: Int)

    fun observeFavourites(): Flow<List<Character>>

    suspend fun getFavourites(): List<Character>
    suspend fun isFavourite(id: Int): Boolean
}

class CharacterRepositoryImpl @Inject constructor(
    private val api: HpApi,
    private val favouriteDao: FavouriteCharacterDao,
) : CharacterRepository {

    override suspend fun getCharacters(): List<Character> =
        withContext(Dispatchers.IO) {
            api.characters().mapIndexedNotNull { i, v ->
                v.dtoToCharacter(i)
            }
        }

    override suspend fun addFavourite(character: Character) =
        withContext(Dispatchers.IO) {
            favouriteDao.insert(
                FavouriteCharacterEntity(
                    id = character.id,
                    name = character.name,
                    gender = character.gender,
                    dateOfBirth = character.dateOfBirth,
                    eyeColour = character.eyeColour,
                    hairColour = character.hairColour,
                    species = character.species,
                    wizard = character.wizard,
                    house = character.house,
                    wandWood = character.wandWood,
                    wandCore = character.wandCore,
                    patronus = character.patronus
                )
            )
        }

    override suspend fun removeFavourite(id: Int) =
        withContext(Dispatchers.IO) {
            favouriteDao.deleteById(id)
        }

    override fun observeFavourites(): Flow<List<Character>> =
        favouriteDao.observeAll()
            .map { list -> list.map { it.toDomain() } }

    override suspend fun getFavourites(): List<Character> =
        withContext(Dispatchers.IO) {
            favouriteDao.getAll().map { it.toDomain() }
        }

    override suspend fun isFavourite(id: Int): Boolean =
        withContext(Dispatchers.IO) {
            favouriteDao.isFavourite(id)
        }
}



