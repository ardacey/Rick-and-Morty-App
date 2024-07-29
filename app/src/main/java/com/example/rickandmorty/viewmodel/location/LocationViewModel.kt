package com.example.rickandmorty.viewmodel.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.model.location.Location
import com.example.rickandmorty.repository.LocationDownload
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LocationViewModel(
    private val repository: LocationDownload
) : ViewModel(), KoinComponent {

    private val preferencesManager: PreferencesManager by inject()

    private val _state = MutableStateFlow(LocationScreenState())
    val state: StateFlow<LocationScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _typeSuggestions = MutableStateFlow<List<String>>(emptyList())
    val typeSuggestions: StateFlow<List<String>> = _typeSuggestions.asStateFlow()

    private val _dimensionSuggestions = MutableStateFlow<List<String>>(emptyList())
    val dimensionSuggestions: StateFlow<List<String>> = _dimensionSuggestions.asStateFlow()

    private var favoriteLocations = emptySet<String>()

    init {
        getLocations()
        observeFavoriteLocations()
    }

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
        _state.value.locations
            .flatMap { it.type.split(",") }
            .filter { it.contains(query, ignoreCase = true) }
            .distinct()
            .let { _typeSuggestions.value = it }
    }

    fun updateDimensionSuggestions(query: String) {
        _state.value.locations
            .flatMap { it.dimension.split(",") }
            .filter { it.contains(query, ignoreCase = true) }
            .distinct()
            .let { _dimensionSuggestions.value = it }
    }

    fun updateOnlyFavorites(onlyFavorites: Boolean) {
        _state.update { it.copy(onlyFavorites = onlyFavorites) }
    }

    private fun observeFavoriteLocations() {
        viewModelScope.launch {
            preferencesManager.favoriteLocationsFlow.collect { favoriteLocations ->
                this@LocationViewModel.favoriteLocations = favoriteLocations
                _state.update { it.copy(favoriteLocationIds = favoriteLocations) }
            }
        }
    }

    fun toggleFavoriteLocation(locationId: String) {
        viewModelScope.launch {
            if (favoriteLocations.contains(locationId)) {
                preferencesManager.removeFavoriteLocation(locationId)
            } else {
                preferencesManager.addFavoriteLocation(locationId)
            }
        }
    }

    private fun getLocations() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val firstPageResponse = repository.getLocationList(1)
                val totalPages = firstPageResponse.info.pages

                val locationList = (1..totalPages).map { currentPage ->
                    async {
                        repository.getLocationList(currentPage).results
                    }
                }.awaitAll().flatten()

                _state.update { it.copy(locations = locationList) }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class LocationScreenState(
    val locations: List<Location> = emptyList(),
    val favoriteLocationIds: Set<String> = emptySet(),
    val searchQuery: String = "",
    val typeQuery: String = "",
    val dimensionQuery: String = "",
    val onlyFavorites: Boolean = false
) {
    val filteredLocations: List<Location>
        get() = locations.filter { location ->
            location.name.contains(searchQuery, ignoreCase = true) &&
            (typeQuery.isEmpty() || location.type.equals(typeQuery, ignoreCase = true)) &&
            (dimensionQuery.isEmpty() || location.dimension.equals(dimensionQuery, ignoreCase = true)) &&
            (!onlyFavorites || favoriteLocationIds.contains(location.id.toString()))
        }
}