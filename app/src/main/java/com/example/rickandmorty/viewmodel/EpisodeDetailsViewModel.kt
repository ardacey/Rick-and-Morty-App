package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Episode
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(EpisodeDetailsScreenState())

    fun getEpisode(id : Int) {
        viewModelScope.launch {
            val response = repository.getEpisode(id)
            state = state.copy(
                episode = response.body()!!
            )
        }
    }
}

data class EpisodeDetailsScreenState(
    val episode: Episode = Episode()
)