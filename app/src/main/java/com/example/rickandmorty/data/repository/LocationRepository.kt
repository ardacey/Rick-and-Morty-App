package com.example.rickandmorty.data.repository

import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.example.rickandmorty.data.network.model.location.Location
import com.example.rickandmorty.data.network.model.location.LocationList
import com.example.rickandmorty.data.network.ApiCallHandler
import com.example.rickandmorty.data.network.service.LocationAPI

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