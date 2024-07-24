package com.example.rickandmorty.repository

import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.CharacterList
import com.example.rickandmorty.service.CharacterAPI
import com.example.rickandmorty.util.Resource

interface CharacterDownload {
    suspend fun getCharacterList(page: Int): Resource<CharacterList>
    suspend fun getCharacter(id: Int): Resource<Character>
}

class CharacterDownloadImpl(private val api: CharacterAPI) : CharacterDownload {
    override suspend fun getCharacterList(page: Int): Resource<CharacterList> {
        return try {
            val response = api.getCharacters(page)
            if (response.isSuccessful && response.body() != null) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(Exception("Error fetching characters: ${response.message()}"))
            }
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun getCharacter(id: Int): Resource<Character> {
        return try {
            val response = api.getCharacter(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(Exception("Error fetching character: ${response.message()}"))
            }
        } catch (e: Exception) {
            Resource.error(e)
        }
    }
}