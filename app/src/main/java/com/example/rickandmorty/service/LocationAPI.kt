package com.example.rickandmorty.service

import com.example.rickandmorty.model.LocationList
import retrofit2.Response
import retrofit2.http.GET

interface LocationAPI {

    @GET("locations?")
    suspend fun getLocations():Response<LocationList>
}