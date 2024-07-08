package com.example.rickandmorty.viewmodel

import com.example.rickandmorty.model.CharacterList
import com.example.rickandmorty.model.EpisodeList
import com.example.rickandmorty.model.LocationList
import com.example.rickandmorty.util.RetrofitCharacterInstance
import com.example.rickandmorty.util.RetrofitEpisodeInstance
import com.example.rickandmorty.util.RetrofitLocationInstance
import retrofit2.Response

class Repository {
    suspend fun getCharacterList(): Response<CharacterList>{
        return RetrofitCharacterInstance.api.getCharacters()
    }

    suspend fun getLocationList(): Response<LocationList>{
        return RetrofitLocationInstance.api.getLocations()
    }

    suspend fun getEpisodeList(): Response<EpisodeList>{
        return RetrofitEpisodeInstance.api.getEpisodes()
    }
}