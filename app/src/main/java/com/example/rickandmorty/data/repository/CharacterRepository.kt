package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.example.rickandmorty.data.network.model.character.Character
import com.example.rickandmorty.data.network.model.character.CharacterList
import com.example.rickandmorty.data.network.ApiCallHandler
import com.example.rickandmorty.data.network.service.CharacterAPI

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