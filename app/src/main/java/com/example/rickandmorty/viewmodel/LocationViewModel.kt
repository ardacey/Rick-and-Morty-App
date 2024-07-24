package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.repository.LocationDownload
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: LocationDownload
) : ViewModel() {

    var state by mutableStateOf(LocationScreenState())
    val error = MutableLiveData<Resource<Exception>>()
    val isLoading = MutableLiveData<Resource<Boolean>>()

    init { getLocation() }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    private fun getLocation() {
        isLoading.value = Resource.loading(true)
        viewModelScope.launch {
            val firstPageResponse = repository.getLocationList(1)
            isLoading.value = Resource.loading(false)
            val totalPages = firstPageResponse.data?.info?.pages ?: 1

            val deferredList = (1..totalPages).map { currentPage ->
                async {
                    val response = repository.getLocationList(currentPage)
                    response.data?.results.orEmpty()
                }
            }

            isLoading.value = Resource.loading(true)
            val locationList = deferredList.awaitAll().flatten()
            state = state.copy(locations = locationList)
        }
    }
}

data class LocationScreenState(
    val locations: List<Location> = emptyList(),
    val searchQuery: String = ""
) {
    val filteredLocations: List<Location>
        get() = locations.filter { location ->
            location.name.contains(searchQuery, ignoreCase = true)
        }
}