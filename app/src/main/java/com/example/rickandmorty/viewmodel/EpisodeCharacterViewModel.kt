package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.repository.CharacterDownload
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class EpisodeCharacterViewModel(
    private val repository: CharacterDownload
) : ViewModel() {

    private val _state = MutableStateFlow(EpisodeCharacterScreenState())
    val state: StateFlow<EpisodeCharacterScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<Resource<Exception>?>(null)
    val error: StateFlow<Resource<Exception>?> = _error.asStateFlow()

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

                val characters = deferredCharacters.map { it.await().data!! }
                _state.update { it.copy(characters = characters) }
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

data class EpisodeCharacterScreenState(
    val characters : List<Character> = emptyList(),
)