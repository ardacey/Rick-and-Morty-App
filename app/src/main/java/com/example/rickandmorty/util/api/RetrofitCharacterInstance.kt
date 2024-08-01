package com.example.rickandmorty.util.api

import com.example.rickandmorty.service.API.CharacterAPI
import com.example.rickandmorty.util.Util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitCharacterInstance {
    val api: CharacterAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(CharacterAPI::class.java)
    }
}