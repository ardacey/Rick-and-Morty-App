package com.example.rickandmorty.viewmodel

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.repository.EpisodeDownload
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class EpisodeViewModel(
    private val repository: EpisodeDownload
) : ViewModel() {

    private val _state = MutableStateFlow(EpisodeScreenState())
    val state: StateFlow<EpisodeScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<Resource<Exception>?>(null)
    val error: StateFlow<Resource<Exception>?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    init { getEpisode() }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun updateStartDate(startDate: LocalDate) {
        _state.update { it.copy(startDate = startDate) }
    }

    fun updateEndDate(endDate: LocalDate) {
        _state.update { it.copy(endDate = endDate) }
    }

    private fun getEpisode() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val firstPageResponse = repository.getEpisodeList(1)
                val totalPages = firstPageResponse.data?.info?.pages ?: 1

                val deferredList = (1..totalPages).map { currentPage ->
                    async {
                        repository.getEpisodeList(currentPage).data?.results.orEmpty()
                    }
                }

                val episodeList = deferredList.awaitAll().flatten()
                _state.update { it.copy(episodes = episodeList) }
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

data class EpisodeScreenState(
    val episodes: List<Episode> = emptyList(),
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