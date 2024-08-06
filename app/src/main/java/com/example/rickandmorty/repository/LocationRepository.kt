package com.example.rickandmorty.repository

import com.example.rickandmorty.data.model.basemodel.AppResult
import com.example.rickandmorty.data.model.location.Location
import com.example.rickandmorty.data.model.location.LocationList
import com.example.rickandmorty.network.ApiCallHandler
import com.example.rickandmorty.service.api.LocationAPI

interface LocationRepository {
    suspend fun getLocationList(page: Int): AppResult<LocationList>
    suspend fun getLocation(id: Int): AppResult<Location>
}

class LocationRepositoryImpl(private val api: LocationAPI) : LocationRepository {
    override suspend fun getLocationList(page: Int): AppResult<LocationList> {
        return ApiCallHandler.execute { api.getLocations(page) }
    }

    override suspend fun getLocation(id: Int): AppResult<Location> {
        return ApiCallHandler.execute { api.getLocation(id) }
    }
}