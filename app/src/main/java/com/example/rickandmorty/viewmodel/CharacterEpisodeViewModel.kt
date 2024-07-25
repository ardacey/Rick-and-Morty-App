package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.repository.EpisodeDownload
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterEpisodeViewModel(
    private val repository: EpisodeDownload
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterEpisodeScreenState())
    val state: StateFlow<CharacterEpisodeScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<Resource<Exception>?>(null)
    val error: StateFlow<Resource<Exception>?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getEpisodes(episodeURLs: List<String>) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val episodeIDs = episodeURLs.map { it.substringAfterLast("/").toInt() }
                val deferredEpisodes = episodeIDs.map { id ->
                    async {
                        repository.getEpisode(id)
                    }
                }

                val episodes = deferredEpisodes.map { it.await().data!! }
                _state.update { it.copy(episodes = episodes) }
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

data class CharacterEpisodeScreenState(
    val episodes : List<Episode> = emptyList(),
)