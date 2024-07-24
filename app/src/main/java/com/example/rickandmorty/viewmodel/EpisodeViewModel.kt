package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.repository.EpisodeDownload
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class EpisodeViewModel(
    private val repository: EpisodeDownload
) : ViewModel()  {

    var state by mutableStateOf(EpisodeScreenState())
    val error = MutableLiveData<Resource<Exception>>()
    val isLoading = MutableLiveData<Resource<Boolean>>()

    init { getEpisode() }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    private fun getEpisode() {
        isLoading.value = Resource.loading(true)
        viewModelScope.launch {
            val firstPageResponse = repository.getEpisodeList(1)
            isLoading.value = Resource.loading(false)
            val totalPages = firstPageResponse.data?.info?.pages ?: 1

            val deferredList = (1..totalPages).map { currentPage ->
                async {
                    val response = repository.getEpisodeList(currentPage)
                    response.data?.results.orEmpty()
                }
            }

            isLoading.value = Resource.loading(true)
            val episodeList = deferredList.awaitAll().flatten()
            state = state.copy(episodes = episodeList)
        }
    }
}

data class EpisodeScreenState(
    val episodes: List<Episode> = emptyList(),
    val searchQuery: String = ""
) {
    val filteredEpisodes: List<Episode>
        get() = episodes.filter { episode ->
            episode.name.contains(searchQuery, ignoreCase = true)
        }
}