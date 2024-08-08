package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.example.rickandmorty.data.network.model.episode.Episode
import com.example.rickandmorty.data.network.model.episode.EpisodeList
import com.example.rickandmorty.data.network.ApiCallHandler
import com.example.rickandmorty.data.network.service.EpisodeAPI

interface EpisodeRepository {
    suspend fun getEpisodeList(page: Int): AppResult<EpisodeList>
    suspend fun getEpisode(id: Int): AppResult<Episode>
}

class EpisodeRepositoryImpl(private val api: EpisodeAPI) : EpisodeRepository {
    override suspend fun getEpisodeList(page: Int): AppResult<EpisodeList> {
        return ApiCallHandler.execute { api.getEpisodes(page) }
    }

    override suspend fun getEpisode(id: Int): AppResult<Episode> {
        return ApiCallHandler.execute { api.getEpisode(id) }
    }
}