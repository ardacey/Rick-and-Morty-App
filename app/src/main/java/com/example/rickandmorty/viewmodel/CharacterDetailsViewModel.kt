package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.repository.CharacterDownload
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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getCharacter(id : Int) {
        _isLoading.value = true
        viewModelScope.launch {
            val response = repository.getCharacter(id)
            if (response.error != null) {
                _error.value = response.error.message
                return@launch
            }
            _state.update { it.copy(character = response.data!!) }
            _isLoading.value = false
        }
    }
}

data class CharacterDetailsScreenState(
    val character: Character = Character(),
)