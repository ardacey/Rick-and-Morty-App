package com.example.rickandmorty.repository

import com.example.rickandmorty.model.episode.Episode
import com.example.rickandmorty.model.episode.EpisodeList
import com.example.rickandmorty.service.API.EpisodeAPI

interface EpisodeDownload {
    suspend fun getEpisodeList(page: Int): EpisodeList
    suspend fun getEpisode(id: Int): Episode
}

class EpisodeDownloadImpl(private val api: EpisodeAPI) : EpisodeDownload {
    override suspend fun getEpisodeList(page: Int): EpisodeList {
        val response = api.getEpisodes(page)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error fetching episodes: ${response.message()}")
        }
    }

    override suspend fun getEpisode(id: Int): Episode {
        val response = api.getEpisode(id)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error fetching episode: ${response.message()}")
        }
    }
}