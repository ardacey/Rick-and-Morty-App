package com.example.rickandmorty.viewmodel.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.repository.LocationDownload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationDetailsViewModel(
    private val repository: LocationDownload
) : ViewModel() {

    private val _state = MutableStateFlow(LocationDetailsScreenState())
    val state: StateFlow<LocationDetailsScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getLocation(id : Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getLocation(id)
                _state.update { it.copy(location = response) }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class LocationDetailsScreenState(
    val location: Location = Location(),
)