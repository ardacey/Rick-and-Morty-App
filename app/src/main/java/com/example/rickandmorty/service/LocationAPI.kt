package com.example.rickandmorty.service

import com.example.rickandmorty.model.LocationList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationAPI {

    @GET("locations?")
    suspend fun getLocations(@Query("page") page: Int):Response<LocationList>
}