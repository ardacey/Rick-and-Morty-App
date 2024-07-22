package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class EpisodeViewModel : ViewModel()  {
    private val repository = Repository()
    var state by mutableStateOf(EpisodeScreenState())

    init { getEpisode() }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    private fun getEpisode() {
        viewModelScope.launch {
            val allEpisodes = mutableListOf<Episode>()
            val firstPageResponse = repository.getEpisodeList(1)
            val totalPages = firstPageResponse.body()?.info?.pages ?: 1

            val deferredList = (1..totalPages).map { currentPage ->
                async {
                    val response = repository.getEpisodeList(currentPage)
                    response.body()?.results.orEmpty()
                }
            }

            val episodeList = deferredList.awaitAll().flatten()
            allEpisodes.addAll(episodeList)
            state = state.copy(episodes = allEpisodes)
        }
    }
}

data class EpisodeScreenState(
    val episodes: List<Episode> = emptyList(),
    val searchQuery: String = ""
)