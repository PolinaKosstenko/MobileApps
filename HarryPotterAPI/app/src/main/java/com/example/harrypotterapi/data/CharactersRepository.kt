package com.example.harrypotterapi.data

import com.example.harrypotterapi.NetworkModule
import com.example.harrypotterapi.data.remote.HpApi
import com.example.harrypotterapi.data.remote.dtoToCharacter
import com.example.harrypotterapi.model.Character
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class CharactersRepository(private val api: HpApi = NetworkModule.api) {
    suspend fun characters(): List<Character> = withContext(Dispatchers.IO) {
        api.characters().mapIndexedNotNull { i, v -> v.dtoToCharacter(i) }
    }
}
