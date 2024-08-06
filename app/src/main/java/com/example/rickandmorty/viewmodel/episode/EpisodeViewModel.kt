package com.example.rickandmorty.viewmodel.episode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.data.model.basemodel.AppResult
import com.example.rickandmorty.data.model.episode.Episode
import com.example.rickandmorty.repository.EpisodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EpisodeViewModel(
    private val repository: EpisodeRepository
) : ViewModel(), KoinComponent {

    private val preferencesManager: PreferencesManager by inject()

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

    fun updateStartDate(startDate: LocalDate) {
        _state.update { it.copy(startDate = startDate) }
    }

    fun updateEndDate(endDate: LocalDate) {
        _state.update { it.copy(endDate = endDate) }
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
                    episodeList = (1..totalPages).flatMap { page ->
                        when (val response = repository.getEpisodeList(page)) {
                            is AppResult.Success -> response.successData.results
                            is AppResult.Error -> emptyList()
                        }
                    }
                }
                is AppResult.Error -> {
                    _error.value = firstPageResult.message
                }
            }
            _state.update { it.copy(episodes = episodeList) }
            _state.update { it.copy(loading = false) }
        }
    }
}

data class EpisodeScreenState(
    val episodes: List<Episode> = emptyList(),
    val favoriteEpisodeIds: Set<String> = emptySet(),
    val searchQuery: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val onlyFavorites: Boolean = false,
    val loading: Boolean = false
) {
    val filteredEpisodes: List<Episode>
        get() = episodes.filter { episode ->
            val matchesQuery = episode.name.contains(searchQuery, ignoreCase = true)
            val airDate = toLocalDate(episode.airDate)
            val withinDateRange = (startDate == null || airDate >= startDate) &&
                    (endDate == null || airDate <= endDate)
            val isFavorite = favoriteEpisodeIds.contains(episode.id.toString())

            matchesQuery && withinDateRange && (!onlyFavorites || isFavorite)
        }

    private fun toLocalDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        return LocalDate.parse(dateString, formatter)
    }
}