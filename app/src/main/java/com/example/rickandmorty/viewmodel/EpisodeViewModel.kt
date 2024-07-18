package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import kotlinx.coroutines.launch

class EpisodeViewModel : ViewModel()  {
    private val repository = Repository()
    var state by mutableStateOf(EpisodeScreenState())
    var id by mutableIntStateOf(0)

    init { getEpisode() }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    private fun getEpisode() {
        viewModelScope.launch {
            val response = repository.getEpisodeList(state.page)
            state = state.copy(
                episodes = response.body()!!.results
            )
        }
    }
}

data class EpisodeScreenState(
    val episodes: List<Episode> = emptyList(),
    val searchQuery: String = "",
    val page: Int = 1,
)