package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Location
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(LocationScreenState())

    init { getLocation() }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    private fun getLocation() {
        viewModelScope.launch {
            val allLocations = mutableListOf<Location>()
            val firstPageResponse = repository.getLocationList(1)
            val totalPages = firstPageResponse.body()?.info?.pages ?: 1

            val deferredList = (1..totalPages).map { currentPage ->
                async {
                    val response = repository.getLocationList(currentPage)
                    response.body()?.results.orEmpty()
                }
            }

            val locationList = deferredList.awaitAll().flatten()
            allLocations.addAll(locationList)
            state = state.copy(locations = allLocations)
        }
    }
}

data class LocationScreenState(
    val locations: List<Location> = emptyList(),
    val searchQuery: String = ""
)