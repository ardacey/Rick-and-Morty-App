package com.example.rickandmorty.service.api

import com.example.rickandmorty.data.model.episode.Episode
import com.example.rickandmorty.data.model.episode.EpisodeList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface EpisodeAPI {

    @GET("episode?")
    suspend fun getEpisodes(@Query("page") page: Int):Response<EpisodeList>

    @GET("episode/{id}")
    suspend fun getEpisode(@Path("id") id: Int):Response<Episode>

}