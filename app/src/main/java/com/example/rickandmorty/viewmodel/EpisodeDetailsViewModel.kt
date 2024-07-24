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
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel(
    private val repository: EpisodeDownload
) : ViewModel() {

    var state by mutableStateOf(EpisodeDetailsScreenState())
    val error = MutableLiveData<Resource<Exception>>()
    val isLoading = MutableLiveData<Resource<Boolean>>()

    fun getEpisode(id : Int) {
        isLoading.value = Resource.loading(true)
        viewModelScope.launch {
            val response = repository.getEpisode(id)
            isLoading.value = Resource.loading(false)

            state = state.copy(
                episode = response.data!!
            )
        }
    }
}

data class EpisodeDetailsScreenState(
    val episode: Episode = Episode()
)