package com.example.harrypotterapi.data

import com.example.harrypotterapi.NetworkModule
import com.example.harrypotterapi.data.local.FavouriteCharacterDao
import com.example.harrypotterapi.data.local.FavouriteCharacterEntity
import com.example.harrypotterapi.data.local.toDomain
import com.example.harrypotterapi.data.remote.HpApi
import com.example.harrypotterapi.data.remote.dtoToCharacter
import com.example.harrypotterapi.model.Character
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

interface CharacterRepository {
    suspend fun getCharacters(): List<Character>
    suspend fun addFavourite(character: Character)
    suspend fun removeFavourite(id: Int)
    suspend fun getFavourites(): List<Character>
}

class CharacterRepositoryImpl @Inject constructor(
    private val api: HpApi,
    private val favouriteDao: FavouriteCharacterDao,
) : CharacterRepository {
    override suspend fun addFavourite(character: Character) = withContext(Dispatchers.IO) {
        favouriteDao.insert(FavouriteCharacterEntity(
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
        ))
    }

    override suspend fun removeFavourite(id: Int) = withContext(Dispatchers.IO)  {
        favouriteDao.deleteById(id)
    }

    override suspend fun getFavourites(): List<Character> = withContext(Dispatchers.IO) {
        favouriteDao.getAll().map { it.toDomain() }
    }

    override suspend fun getCharacters(): List<Character> =
        withContext(Dispatchers.IO) {
            api.characters().mapIndexedNotNull { i, v ->
                v.dtoToCharacter(i)
            }
        }


}




