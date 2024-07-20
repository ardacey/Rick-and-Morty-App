package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class CharacterEpisodeViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(CharacterEpisodeScreenState())

    fun getEpisodes(episodeURLs: List<String>) {
        viewModelScope.launch {
            val episodeIDs = episodeURLs.map { it.substringAfterLast("/").toInt() }
            val deferredEpisodes = episodeIDs.map { id ->
                async {
                    repository.getEpisode(id)
                }
            }

            state = state.copy(
                episodes = deferredEpisodes.map { it.await().body()!! }
            )
        }
    }
}

data class CharacterEpisodeScreenState(
    val episodes : List<Episode> = emptyList(),
)