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
import kotlinx.coroutines.launch

class CharacterEpisodeViewModel(
    private val repository: EpisodeDownload
) : ViewModel() {

    var state by mutableStateOf(CharacterEpisodeScreenState())
    val error = MutableLiveData<Resource<Exception>>()
    val isLoading = MutableLiveData<Resource<Boolean>>()

    fun getEpisodes(episodeURLs: List<String>) {
        isLoading.value = Resource.loading(true)
        viewModelScope.launch {
            val episodeIDs = episodeURLs.map { it.substringAfterLast("/").toInt() }
            val deferredEpisodes = episodeIDs.map { id ->
                async {
                    repository.getEpisode(id)
                }
            }

            isLoading.value = Resource.loading(false)
            state = state.copy(
                episodes = deferredEpisodes.map { it.await().data!! }
            )
        }
    }
}

data class CharacterEpisodeScreenState(
    val episodes : List<Episode> = emptyList(),
)