package com.example.rickandmorty.repository

import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.model.EpisodeList
import com.example.rickandmorty.service.EpisodeAPI
import com.example.rickandmorty.util.Resource

interface EpisodeDownload {
    suspend fun getEpisodeList(page: Int): Resource<EpisodeList>
    suspend fun getEpisode(id: Int): Resource<Episode>
}

class EpisodeDownloadImpl(private val api: EpisodeAPI) : EpisodeDownload {
    override suspend fun getEpisodeList(page: Int): Resource<EpisodeList> {
        return try {
            val response = api.getEpisodes(page)
            if (response.isSuccessful && response.body() != null) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(Exception("Error fetching episodes: ${response.message()}"))
            }
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun getEpisode(id: Int): Resource<Episode> {
        return try {
            val response = api.getEpisode(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(Exception("Error fetching episode: ${response.message()}"))
            }
        } catch (e: Exception) {
            Resource.error(e)
        }
    }
}