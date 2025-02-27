package com.example.rickandmorty.presentation.ui.episodes_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.example.rickandmorty.data.network.model.episode.Episode
import com.example.rickandmorty.data.repository.EpisodeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EpisodeViewModel(
    private val repository: EpisodeRepository,
    private val preferencesManager: PreferencesManager
) : ViewModel() {

    private val _state = MutableStateFlow(EpisodeScreenState())
    val state: StateFlow<EpisodeScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private var favoriteEpisodes = emptySet<String>()

    init {
        getEpisodes()
        observeFavoriteEpisodes()
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun updateOnlyFavorites(onlyFavorites: Boolean) {
        _state.update { it.copy(onlyFavorites = onlyFavorites) }
    }

    private fun observeFavoriteEpisodes() {
        viewModelScope.launch {
            preferencesManager.favoriteEpisodesFlow.collect { favoriteEpisodes ->
                this@EpisodeViewModel.favoriteEpisodes = favoriteEpisodes
                _state.update { it.copy(favoriteEpisodeIds = favoriteEpisodes) }
            }
        }
    }

    fun toggleFavoriteEpisode(episodeId: String) {
        viewModelScope.launch {
            if (favoriteEpisodes.contains(episodeId)) {
                preferencesManager.removeFavoriteEpisode(episodeId)
            } else {
                preferencesManager.addFavoriteEpisode(episodeId)
            }
        }
    }

    private fun getEpisodes() {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            var episodeList = emptyList<Episode>()
            when (val firstPageResult = repository.getEpisodeList(1)) {
                is AppResult.Success -> {
                    val totalPages = firstPageResult.successData.info.pages
                    val deferredResults = (2..totalPages).map { page ->
                        async {
                            when (val response = repository.getEpisodeList(page)) {
                                is AppResult.Success -> response.successData.results
                                is AppResult.Error -> emptyList()
                            }
                        }
                    }
                    episodeList = firstPageResult.successData.results + deferredResults.awaitAll().flatten()
                }
                is AppResult.Error -> {
                    _error.value = firstPageResult.message
                }
            }
            _state.update { it.copy(episodes = episodeList, loading = false) }
        }
    }
}

data class EpisodeScreenState(
    val episodes: List<Episode> = emptyList(),
    val favoriteEpisodeIds: Set<String> = emptySet(),
    val searchQuery: String = "",
    val onlyFavorites: Boolean = false,
    val loading: Boolean = false
) {
    val filteredEpisodes: List<Episode>
        get() = episodes.filter { episode ->
            val matchesQuery = episode.name.contains(searchQuery, ignoreCase = true)
            val isFavorite = favoriteEpisodeIds.contains(episode.id.toString())

            matchesQuery && (!onlyFavorites || isFavorite)
        }
}