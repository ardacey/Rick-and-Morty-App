package com.example.rickandmorty.repository

import com.example.rickandmorty.model.Location
import com.example.rickandmorty.model.LocationList
import com.example.rickandmorty.service.LocationAPI
import com.example.rickandmorty.util.Resource

interface LocationDownload {
    suspend fun getLocationList(page: Int): Resource<LocationList>
    suspend fun getLocation(id: Int): Resource<Location>
}

class LocationDownloadImpl(private val api: LocationAPI) : LocationDownload {
    override suspend fun getLocationList(page: Int): Resource<LocationList> {
        return try {
            val response = api.getLocations(page)
            if (response.isSuccessful && response.body() != null) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(Exception("Error fetching locations: ${response.message()}"))
            }
        } catch (e: Exception) {
            Resource.error(e)
        }
    }

    override suspend fun getLocation(id: Int): Resource<Location> {
        return try {
            val response = api.getLocation(id)
            if (response.isSuccessful && response.body() != null) {
                Resource.success(response.body()!!)
            } else {
                Resource.error(Exception("Error fetching location: ${response.message()}"))
            }
        } catch (e: Exception) {
            Resource.error(e)
        }
    }
}