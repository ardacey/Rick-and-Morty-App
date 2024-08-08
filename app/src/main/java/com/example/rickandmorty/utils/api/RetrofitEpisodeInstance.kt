package com.example.rickandmorty.utils.api

import com.example.rickandmorty.data.network.service.EpisodeAPI
import com.example.rickandmorty.utils.Util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitEpisodeInstance {
    val api: EpisodeAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(EpisodeAPI::class.java)
    }
}