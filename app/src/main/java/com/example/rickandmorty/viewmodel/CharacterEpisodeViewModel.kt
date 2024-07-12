package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import kotlinx.coroutines.launch

// CharacterEpisodeViewModel çalışmıyor fix lazım

class CharacterEpisodeViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(CharacterEpisodeScreenState())
    var ids by mutableStateOf(listOf<Int>())

    fun setAndFetchEpisodes(episodeIDs: List<Int>) {
        this.ids = episodeIDs
        getEpisodes(episodeIDs)
    }

    private fun getEpisodes(episodeIDs: List<Int>) {
        viewModelScope.launch {
            for (id in episodeIDs) {
                val response = repository.getEpisode(id)
                state = state.copy(
                    episodes = state.episodes + response.body()!!
                )
            }
        }
    }
}

data class CharacterEpisodeScreenState(
    val episodes : List<Episode> = emptyList(),
)