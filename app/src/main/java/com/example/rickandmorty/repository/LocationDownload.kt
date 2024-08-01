package com.example.rickandmorty.repository

import com.example.rickandmorty.model.location.Location
import com.example.rickandmorty.model.location.LocationList
import com.example.rickandmorty.service.API.LocationAPI

interface LocationDownload {
    suspend fun getLocationList(page: Int): LocationList
    suspend fun getLocation(id: Int): Location
}

class LocationDownloadImpl(private val api: LocationAPI) : LocationDownload {
    override suspend fun getLocationList(page: Int): LocationList {
        val response = api.getLocations(page)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error fetching locations: ${response.message()}")
        }
    }

    override suspend fun getLocation(id: Int): Location {
        val response = api.getLocation(id)
        if (response.isSuccessful && response.body() != null) {
            return response.body()!!
        } else {
            throw Exception("Error fetching location: ${response.message()}")
        }
    }
}