package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.repository.CharacterDownload
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val repository: CharacterDownload
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterDetailsScreenState())
    val state: StateFlow<CharacterDetailsScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<Resource<Exception>?>(null)
    val error: StateFlow<Resource<Exception>?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getCharacter(id : Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getCharacter(id)
                _state.update { it.copy(character = response.data!!) }
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

data class CharacterDetailsScreenState(
    val character: Character = Character(),
)