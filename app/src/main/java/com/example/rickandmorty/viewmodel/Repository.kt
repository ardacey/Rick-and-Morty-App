package com.example.rickandmorty.viewmodel

import com.example.rickandmorty.model.CharacterList
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.model.EpisodeList
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.LocationList
import com.example.rickandmorty.util.RetrofitCharacterInstance
import com.example.rickandmorty.util.RetrofitEpisodeInstance
import com.example.rickandmorty.util.RetrofitLocationInstance
import retrofit2.Response

class Repository {
    suspend fun getCharacterList(page: Int): Response<CharacterList>{
        return RetrofitCharacterInstance.api.getCharacters(page)
    }

    suspend fun getCharacter(id: Int): Response<Character>{
        return RetrofitCharacterInstance.api.getCharacter(id)
    }

    suspend fun getLocationList(page: Int): Response<LocationList>{
        return RetrofitLocationInstance.api.getLocations(page)
    }

    suspend fun getLocation(id: Int): Response<Location>{
        return RetrofitLocationInstance.api.getLocation(id)
    }

    suspend fun getEpisodeList(page: Int): Response<EpisodeList>{
        return RetrofitEpisodeInstance.api.getEpisodes(page)
    }

    suspend fun getEpisode(id: Int): Response<Episode>{
        return RetrofitEpisodeInstance.api.getEpisode(id)
    }

}