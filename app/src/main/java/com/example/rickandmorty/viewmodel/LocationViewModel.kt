package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.repository.LocationDownload
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationViewModel(
    private val repository: LocationDownload
) : ViewModel() {

    private val _state = MutableStateFlow(LocationScreenState())
    val state: StateFlow<LocationScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<Resource<Exception>?>(null)
    val error: StateFlow<Resource<Exception>?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _typeSuggestions = MutableStateFlow<List<String>>(emptyList())
    val typeSuggestions: StateFlow<List<String>> = _typeSuggestions.asStateFlow()

    private val _dimensionSuggestions = MutableStateFlow<List<String>>(emptyList())
    val dimensionSuggestions: StateFlow<List<String>> = _dimensionSuggestions.asStateFlow()

    init { getLocation() }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun updateTypeQuery(query: String) {
        _state.update { it.copy(typeQuery = query) }
        updateTypeSuggestions(query)
    }

    fun updateDimensionQuery(query: String) {
        _state.update { it.copy(dimensionQuery = query) }
        updateDimensionSuggestions(query)
    }

    fun updateTypeSuggestions(query: String) {
        val filteredLocations = _state.value.locations.filter {
            it.type.contains(query, ignoreCase = true)
        }
        val newSuggestions = filteredLocations.map { it.type }.distinct()
        _typeSuggestions.value = newSuggestions
    }

    fun updateDimensionSuggestions(query: String) {
        val filteredLocations = _state.value.locations.filter {
            it.dimension.contains(query, ignoreCase = true)
        }
        val newSuggestions = filteredLocations.map { it.dimension }.distinct()
        _dimensionSuggestions.value = newSuggestions
    }

    private fun getLocation() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val firstPageResponse = repository.getLocationList(1)
                val totalPages = firstPageResponse.data?.info?.pages ?: 1

                val deferredList = (1..totalPages).map { currentPage ->
                    async {
                        val response = repository.getLocationList(currentPage)
                        response.data?.results.orEmpty()
                    }
                }

                val locationList = deferredList.awaitAll().flatten()
                _state.update { it.copy(locations = locationList) }
            } catch (e: Exception) {
                _error.value = Resource.error(e)
            } finally {
                _isLoading.value = false
            }
        }
    }
    fun clearError() {
        _error.value = null
    }
}

data class LocationScreenState(
    val locations: List<Location> = emptyList(),
    val searchQuery: String = "",
    val typeQuery: String = "",
    val dimensionQuery: String = "",
) {
    val filteredLocations: List<Location>
        get() = locations.filter { location ->
            location.name.contains(searchQuery, ignoreCase = true) &&
            (typeQuery.isEmpty() || location.type.equals(typeQuery, ignoreCase = true)) &&
            (dimensionQuery.isEmpty() || location.dimension.equals(dimensionQuery, ignoreCase = true))
        }
}