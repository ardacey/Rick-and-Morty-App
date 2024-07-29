package com.example.rickandmorty.viewmodel.character

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.repository.EpisodeDownload
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

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

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

                val episodes = deferredEpisodes.map { it.await() }
                _state.update { it.copy(episodes = episodes) }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class CharacterEpisodeScreenState(
    val episodes : List<Episode> = emptyList(),
)