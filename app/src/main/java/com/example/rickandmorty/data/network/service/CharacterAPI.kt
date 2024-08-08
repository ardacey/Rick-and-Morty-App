package com.example.rickandmorty.data.network.service

import com.example.rickandmorty.data.network.model.character.CharacterList
import com.example.rickandmorty.data.network.model.character.Character
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface CharacterAPI {
    
    @GET("character?")
    suspend fun getCharacters(@Query("page") page: Int):Response<CharacterList>

    @GET("character/{id}")
    suspend fun getCharacter(@Path("id") id: Int):Response<Character>

}