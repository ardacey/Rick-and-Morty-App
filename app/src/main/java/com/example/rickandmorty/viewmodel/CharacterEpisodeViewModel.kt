package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import kotlinx.coroutines.launch

class CharacterEpisodeViewModel(episodeIds: List<Int>) : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(CharacterEpisodeScreenState())

    init { getEpisodes(episodeIds) }

    private fun getEpisodes(episodeIDs: List<Int>) {
        viewModelScope.launch {
            val episodes = mutableListOf<Episode>()
            for (id in episodeIDs) {
                val response = repository.getEpisode(id)
                episodes.add(response.body()!!)
            }
            state = state.copy(
                episodes = episodes
            )
        }
    }
}

data class CharacterEpisodeScreenState(
    val episodes : List<Episode> = emptyList(),
)