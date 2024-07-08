package com.example.rickandmorty.service

import com.example.rickandmorty.model.CharacterList
import retrofit2.Response
import retrofit2.http.GET

interface CharacterAPI {
    
    @GET("character?")
    suspend fun getCharacters():Response<CharacterList>
}