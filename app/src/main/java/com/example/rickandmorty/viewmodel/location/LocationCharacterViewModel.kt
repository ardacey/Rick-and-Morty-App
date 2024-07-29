package com.example.rickandmorty.viewmodel.location

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.example.rickandmorty.model.character.Character
import com.example.rickandmorty.repository.CharacterDownload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class LocationCharacterViewModel(
    private val repository: CharacterDownload
) : ViewModel() {

    private val _state = MutableStateFlow(LocationCharacterScreenState())
    val state: StateFlow<LocationCharacterScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getCharacters(characterURLs: List<String>) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val characterIDs = characterURLs.map { it.substringAfterLast("/").toInt() }
                val deferredCharacters = characterIDs.map { id ->
                    async {
                        repository.getCharacter(id)
                    }
                }

                val characters = deferredCharacters.map { it.await()}
                _state.update { it.copy(characters = characters) }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class LocationCharacterScreenState(
    val characters : List<Character> = emptyList(),
)