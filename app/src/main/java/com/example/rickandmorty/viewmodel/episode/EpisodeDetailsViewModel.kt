package com.example.rickandmorty.viewmodel.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.repository.EpisodeDownload
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel(
    private val repository: EpisodeDownload
) : ViewModel() {

    private val _state = MutableStateFlow(EpisodeDetailsScreenState())
    val state: StateFlow<EpisodeDetailsScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    fun getEpisode(id : Int) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val response = repository.getEpisode(id)
                _state.update { it.copy(episode = response) }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class EpisodeDetailsScreenState(
    val episode: Episode = Episode()
)