package com.example.rickandmorty.service.api

import com.example.rickandmorty.data.model.location.Location
import com.example.rickandmorty.data.model.location.LocationList
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationAPI {

    @GET("location?")
    suspend fun getLocations(@Query("page") page: Int):Response<LocationList>

    @GET("location/{id}")
    suspend fun getLocation(@Path("id") id: Int):Response<Location>
}