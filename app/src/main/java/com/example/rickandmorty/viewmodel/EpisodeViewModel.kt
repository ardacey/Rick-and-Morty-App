package com.example.rickandmorty.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.repository.EpisodeDownload
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
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
    private val repository: EpisodeDownload
) : ViewModel(), KoinComponent {

    private val preferencesManager: PreferencesManager by inject()

    private val _state = MutableStateFlow(EpisodeScreenState())
    val state: StateFlow<EpisodeScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

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
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val firstPageResponse = repository.getEpisodeList(1)
                val totalPages = firstPageResponse.data?.info?.pages ?: 1

                if (firstPageResponse.error != null) {
                    _error.value = firstPageResponse.error.message
                    return@launch
                }

                val episodeList = (1..totalPages).map { currentPage ->
                    async {
                        repository.getEpisodeList(currentPage).data?.results.orEmpty()
                    }
                }.awaitAll().flatten()

                _state.update { it.copy(episodes = episodeList) }
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class EpisodeScreenState(
    val episodes: List<Episode> = emptyList(),
    val favoriteEpisodeIds: Set<String> = emptySet(),
    val searchQuery: String = "",
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null
) {
    val filteredEpisodes: List<Episode>
        @RequiresApi(Build.VERSION_CODES.O)
        get() = episodes.filter { episode ->
            val matchesQuery = episode.name.contains(searchQuery, ignoreCase = true)
            val airDate = toLocalDate(episode.airDate)
            val withinDateRange = (startDate == null || airDate >= startDate) &&
                    (endDate == null || airDate <= endDate)

            matchesQuery && withinDateRange
        }

    @RequiresApi(Build.VERSION_CODES.O)
    fun toLocalDate(dateString: String): LocalDate {
        val formatter = DateTimeFormatter.ofPattern("MMMM d, yyyy")
        return LocalDate.parse(dateString, formatter)
    }
}