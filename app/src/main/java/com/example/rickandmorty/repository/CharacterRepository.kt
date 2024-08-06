package com.example.rickandmorty.repository

import com.example.rickandmorty.data.model.basemodel.AppResult
import com.example.rickandmorty.data.model.character.Character
import com.example.rickandmorty.data.model.character.CharacterList
import com.example.rickandmorty.network.ApiCallHandler
import com.example.rickandmorty.service.api.CharacterAPI

interface CharacterRepository {
    suspend fun getCharacterList(page: Int) : AppResult<CharacterList>
    suspend fun getCharacter(id: Int) : AppResult<Character>
}

class CharacterRepositoryImpl(private val api: CharacterAPI) : CharacterRepository {
    override suspend fun getCharacterList(page: Int) : AppResult<CharacterList> {
        return ApiCallHandler.execute { api.getCharacters(page) }
    }

    override suspend fun getCharacter(id: Int): AppResult<Character> {
        return ApiCallHandler.execute { api.getCharacter(id) }
    }
}