package com.example.rickandmorty.service

import com.example.rickandmorty.model.EpisodeList
import retrofit2.Response
import retrofit2.http.GET

interface EpisodeAPI {

    @GET("episodes?")
    suspend fun getEpisodes():Response<EpisodeList>
}