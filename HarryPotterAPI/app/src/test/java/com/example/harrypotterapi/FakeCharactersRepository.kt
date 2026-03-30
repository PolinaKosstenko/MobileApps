package com.example.harrypotterapi

import com.example.harrypotterapi.data.CharacterRepository
import com.example.harrypotterapi.model.Character

class FakeCharactersRepository : CharacterRepository {

    val favourites : MutableList<Character> = mutableListOf()
    val searchResult : List<Character> = emptyList()

    var failGetFavourites = false
    var failSearch = false

    override suspend fun addFavourite(character: Character) {
        favourites.removeAll {it.id == character.id}
        favourites.add(0, character)
    }

    override suspend fun removeFavourite(id: Int) {
        favourites.removeAll {it.id == id}
    }

    override suspend fun getFavourites(): List<Character> {
        if (failGetFavourites) error("getFavourite failed")
        return favourites.toList()
    }

    override suspend fun getCharacters(): List<Character> {
        if (failSearch) error("search failed")
        return searchResult
    }
}