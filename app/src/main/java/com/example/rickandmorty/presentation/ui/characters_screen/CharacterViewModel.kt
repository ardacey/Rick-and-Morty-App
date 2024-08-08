package com.example.rickandmorty.presentation.ui.characters_screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.example.rickandmorty.data.network.model.character.Character
import com.example.rickandmorty.data.repository.CharacterRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class CharacterViewModel(
    private val repository: CharacterRepository
) : ViewModel(), KoinComponent {

    private val preferencesManager: PreferencesManager by inject()

    private val _state = MutableStateFlow(CharacterScreenState())
    val state: StateFlow<CharacterScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _speciesSuggestions = MutableStateFlow<List<String>>(emptyList())
    val speciesSuggestions: StateFlow<List<String>> = _speciesSuggestions.asStateFlow()

    private val _typeSuggestions = MutableStateFlow<List<String>>(emptyList())
    val typeSuggestions: StateFlow<List<String>> = _typeSuggestions.asStateFlow()

    private var favoriteCharacters = emptySet<String>()

    init {
        getCharacters()
        observeFavoriteCharacters()
    }

    fun updateSearchQuery(query: String) {
        _state.update { it.copy(searchQuery = query) }
    }

    fun updateStatusFilter(filter: String) {
        _state.update { it.copy(statusFilter = filter) }
    }

    fun updateGenderFilter(filter: String) {
        _state.update { it.copy(genderFilter = filter) }
    }

    fun updateSpeciesQuery(query: String) {
        _state.update { it.copy(speciesQuery = query) }
        updateSpeciesSuggestions(query)
    }

    fun updateTypeQuery(query: String) {
        _state.update { it.copy(typeQuery = query) }
        updateTypeSuggestions(query)
    }

    fun updateSpeciesSuggestions(query: String) {
        _state.value.characters
            .flatMap { it.species.split(",") }
            .filter { it.contains(query, ignoreCase = true) }
            .distinct()
            .let { _speciesSuggestions.value = it }
    }

    fun updateTypeSuggestions(query: String) {
        _state.value.characters
            .flatMap { it.type.split(",") }
            .filter { it.contains(query, ignoreCase = true) }
            .distinct()
            .let { _typeSuggestions.value = it }
    }

    fun updateOnlyFavorites(onlyFavorites: Boolean) {
        _state.update { it.copy(onlyFavorites = onlyFavorites) }
    }

    private fun observeFavoriteCharacters() {
        viewModelScope.launch {
            preferencesManager.favoriteCharactersFlow.collectLatest { favoriteCharacters ->
                this@CharacterViewModel.favoriteCharacters = favoriteCharacters
                _state.update { it.copy(favoriteCharacterIds = favoriteCharacters) }
            }
        }
    }

    fun toggleFavoriteCharacter(characterId: String) {
        viewModelScope.launch {
            if (favoriteCharacters.contains(characterId)) {
                preferencesManager.removeFavoriteCharacter(characterId)
            } else {
                preferencesManager.addFavoriteCharacter(characterId)
            }
        }
    }

    private fun getCharacters() {
        _state.update { it.copy(loading = true) }
        viewModelScope.launch {
            var characterList = emptyList<Character>()
            when (val firstPageResult = repository.getCharacterList(1)) {
                is AppResult.Success -> {
                    val totalPages = firstPageResult.successData.info.pages
                    characterList = (1..totalPages).flatMap { page ->
                        when (val response = repository.getCharacterList(page)) {
                            is AppResult.Success -> response.successData.results
                            is AppResult.Error -> emptyList()
                        }
                    }
                }
                is AppResult.Error -> {
                    _error.value = firstPageResult.message
                }
            }
            _state.update { it.copy(characters = characterList) }
            _state.update { it.copy(loading = false) }
        }
    }
}

data class CharacterScreenState(
    val characters: List<Character> = emptyList(),
    val favoriteCharacterIds: Set<String> = emptySet(),
    val searchQuery: String = "",
    val speciesQuery: String = "",
    val typeQuery: String = "",
    val statusFilter: String = "",
    val genderFilter: String = "",
    val onlyFavorites: Boolean = false,
    val loading: Boolean = false
) {
    val filteredCharacters: List<Character>
        get() = characters.filter { character ->
            character.name.contains(searchQuery, ignoreCase = true) &&
            (statusFilter.isEmpty() || character.status.equals(statusFilter, ignoreCase = true)) &&
            (genderFilter.isEmpty() || character.gender.equals(genderFilter, ignoreCase = true)) &&
            (speciesQuery.isEmpty() || character.species.equals(speciesQuery, ignoreCase = true)) &&
            (typeQuery.isEmpty() || character.type.equals(typeQuery, ignoreCase = true)) &&
            (!onlyFavorites || favoriteCharacterIds.contains(character.id.toString()))
        }
}