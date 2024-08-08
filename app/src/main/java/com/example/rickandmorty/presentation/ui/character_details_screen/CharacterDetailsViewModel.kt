package com.example.rickandmorty.presentation.ui.character_details_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.example.rickandmorty.data.network.model.character.Character
import com.example.rickandmorty.data.network.model.episode.Episode
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.repository.EpisodeRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    id: Int,
    private val characterRepository: CharacterRepository,
    private val episodeRepository: EpisodeRepository
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterDetailsScreenState())
    val state: StateFlow<CharacterDetailsScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    init { getCharacter(id) }

    private fun getCharacter(id : Int) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            when (val response = characterRepository.getCharacter(id)) {
                is AppResult.Success -> {
                    getEpisodes(response.successData.episode)
                    _state.update { it.copy(character = response.successData) }
                }
                is AppResult.Error -> {
                    _error.value = response.message
                }
            }
            _state.update { it.copy(loading = false) }
        }
    }

    private fun getEpisodes(episodeURLs: List<String>) {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            val episodeIDs = episodeURLs.map { it.substringAfterLast("/").toInt() }
            val episodeList = episodeIDs.flatMap { id ->
                when (val response = episodeRepository.getEpisode(id)) {
                    is AppResult.Success -> listOf(response.successData)
                    is AppResult.Error -> emptyList()
                }
            }

            _state.update { it.copy(episodes = episodeList) }
            _state.update { it.copy(loading = false) }
        }
    }
}

data class CharacterDetailsScreenState(
    val character: Character = Character(),
    val episodes: List<Episode> = emptyList(),
    val loading: Boolean = false
)