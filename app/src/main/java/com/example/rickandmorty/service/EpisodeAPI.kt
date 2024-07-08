package com.example.rickandmorty.service

import com.example.rickandmorty.model.EpisodeList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface EpisodeAPI {

    @GET("episodes?")
    suspend fun getEpisodes(@Query("page") page: Int):Response<EpisodeList>
}