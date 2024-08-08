package com.example.rickandmorty.presentation.ui.location_details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.network.model.character.Character
import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.example.rickandmorty.data.network.model.location.Location
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.repository.LocationRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class LocationDetailsViewModel(
    id: Int,
    private val locationRepository: LocationRepository,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(LocationDetailsScreenState())
    val state: StateFlow<LocationDetailsScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { getLocation(id) }

    private fun getLocation(id : Int) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            when (val response = locationRepository.getLocation(id)) {
                is AppResult.Success -> {
                    getCharacters(response.successData.residents)
                    _state.update { it.copy(location = response.successData) }
                }
                is AppResult.Error -> {
                    _error.value = response.message
                }
            }
            _state.update { it.copy(loading = false) }
        }
    }

    private fun getCharacters(characterURLs: List<String>) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            val characterIDs = characterURLs.map { it.substringAfterLast("/").toInt() }
            val characterList = characterIDs.flatMap { id ->
                when (val response = characterRepository.getCharacter(id)) {
                    is AppResult.Success -> listOf(response.successData)
                    is AppResult.Error -> emptyList()
                }
            }

            _state.update { it.copy(characters = characterList) }
            _state.update { it.copy(loading = false) }
        }
    }
}

data class LocationDetailsScreenState(
    val characters: List<Character> = emptyList(),
    val location: Location = Location(),
    val loading: Boolean = false
)