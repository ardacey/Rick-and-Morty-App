package com.example.rickandmorty.presentation.ui.locations_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.example.rickandmorty.data.network.model.location.Location
import com.example.rickandmorty.data.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LocationViewModel(
    private val repository: LocationRepository
) : ViewModel(), KoinComponent {

    private val preferencesManager: PreferencesManager by inject()

    private val _state = MutableStateFlow(LocationScreenState())
    val state: StateFlow<LocationScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            var locationList = emptyList<Location>()
            when (val firstPageResult = repository.getLocationList(1)) {
                is AppResult.Success -> {
                    val totalPages = firstPageResult.successData.info.pages
                    locationList = (1..totalPages).flatMap { page ->
                        when (val response = repository.getLocationList(page)) {
                            is AppResult.Success -> response.successData.results
                            is AppResult.Error -> emptyList()
                        }
                    }
                }
                is AppResult.Error -> {
                    _error.value = firstPageResult.message
                }
            }
            _state.update { it.copy(locations = locationList) }
            _state.update { it.copy(loading = false) }
        }
    }
}

data class LocationScreenState(
    val locations: List<Location> = emptyList(),
    val favoriteLocationIds: Set<String> = emptySet(),
    val searchQuery: String = "",
    val typeQuery: String = "",
    val dimensionQuery: String = "",
    val onlyFavorites: Boolean = false,
    val loading: Boolean = false
) {
    val filteredLocations: List<Location>
        get() = locations.filter { location ->
            location.name.contains(searchQuery, ignoreCase = true) &&
            (typeQuery.isEmpty() || location.type.equals(typeQuery, ignoreCase = true)) &&
            (dimensionQuery.isEmpty() || location.dimension.equals(dimensionQuery, ignoreCase = true)) &&
            (!onlyFavorites || favoriteLocationIds.contains(location.id.toString()))
        }
}