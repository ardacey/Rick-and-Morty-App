package com.example.rickandmorty.repository

import com.example.rickandmorty.model.character.Character
import com.example.rickandmorty.model.character.CharacterList
import com.example.rickandmorty.service.API.CharacterAPI

interface CharacterDownload {
    suspend fun getCharacterList(page: Int) : CharacterList
    suspend fun getCharacter(id: Int) : Character
}

class CharacterDownloadImpl(private val api: CharacterAPI) : CharacterDownload {
    override suspend fun getCharacterList(page: Int) : CharacterList {
        val response = api.getCharacters(page)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error fetching characters: ${response.message()}")
        }
    }

    override suspend fun getCharacter(id: Int): Character {
        val response = api.getCharacter(id)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error fetching character: ${response.message()}")
        }
    }
}