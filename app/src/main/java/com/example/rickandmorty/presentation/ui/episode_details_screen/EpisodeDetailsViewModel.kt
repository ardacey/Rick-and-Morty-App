package com.example.rickandmorty.presentation.ui.episode_details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.network.model.character.Character
import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.example.rickandmorty.data.network.model.episode.Episode
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.repository.EpisodeRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class EpisodeDetailsViewModel(
    id: Int,
    private val episodeRepository: EpisodeRepository,
    private val characterRepository: CharacterRepository
) : ViewModel() {

    private val _state = MutableStateFlow(EpisodeDetailsScreenState())
    val state: StateFlow<EpisodeDetailsScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { getEpisode(id) }

    private fun getEpisode(id : Int) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            when (val response = episodeRepository.getEpisode(id)) {
                is AppResult.Success -> {
                    getCharacters(response.successData.characters)
                    _state.update { it.copy(episode = response.successData) }
                }
                is AppResult.Error -> {
                    _error.value = response.message
                }
            }
            _state.update { it.copy(loading = false) }
        }
    }

    private fun getCharacters(characterURLs: List<String>) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            val characterIDs = characterURLs.map { it.substringAfterLast("/").toInt() }
            val deferredResults = characterIDs.map { id ->
                async {
                    when (val response = characterRepository.getCharacter(id)) {
                        is AppResult.Success -> listOf(response.successData)
                        is AppResult.Error -> emptyList()
                    }
                }
            }

            val characterList = deferredResults.awaitAll().flatten()
            _state.update { it.copy(characters = characterList, loading = false) }
        }
    }
}

data class EpisodeDetailsScreenState(
    val characters: List<Character> = emptyList(),
    val episode: Episode = Episode(),
    val loading: Boolean = false
)