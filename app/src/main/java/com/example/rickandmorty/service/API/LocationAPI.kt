package com.example.rickandmorty.service.API

import com.example.rickandmorty.model.location.Location
import com.example.rickandmorty.model.location.LocationList
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