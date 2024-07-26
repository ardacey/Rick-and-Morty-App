package com.example.rickandmorty.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.repository.CharacterDownload
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val repository: CharacterDownload
) : ViewModel() {

    private val _state = MutableStateFlow(CharacterScreenState())
    val state: StateFlow<CharacterScreenState> = _state.asStateFlow()

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error.asStateFlow()

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _speciesSuggestions = MutableStateFlow<List<String>>(emptyList())
    val speciesSuggestions: StateFlow<List<String>> = _speciesSuggestions.asStateFlow()

    private val _typeSuggestions = MutableStateFlow<List<String>>(emptyList())
    val typeSuggestions: StateFlow<List<String>> = _typeSuggestions.asStateFlow()

    init { getCharacter() }

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
        val filteredCharacters = _state.value.characters.filter {
            it.species.contains(query, ignoreCase = true)
        }
        val newSuggestions = filteredCharacters.map { it.species }.distinct()
        _speciesSuggestions.value = newSuggestions
    }

    fun updateTypeSuggestions(query: String) {
        val filteredCharacters = _state.value.characters.filter {
            it.type.contains(query, ignoreCase = true)
        }
        val newSuggestions = filteredCharacters.map { it.type }.distinct()
        _typeSuggestions.value = newSuggestions
    }

    private fun getCharacter() {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val firstPageResponse = repository.getCharacterList(1)
                val totalPages = firstPageResponse.data?.info?.pages ?: 1

                if (firstPageResponse.error != null) {
                    _error.value = firstPageResponse.error.message
                    return@launch
                }

                val deferredList = (1..totalPages).map { currentPage ->
                    async {
                        val response = repository.getCharacterList(currentPage)
                        if (response.error != null) {
                            throw Exception(response.error.message)
                        }
                        response.data?.results.orEmpty()
                    }
                }

                val characterList = deferredList.awaitAll().flatten()
                _state.update { it.copy(characters = characterList) }
            } catch (e : Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }
        }
    }
}

data class CharacterScreenState(
    val characters: List<Character> = emptyList(),
    val searchQuery: String = "",
    val speciesQuery: String = "",
    val typeQuery: String = "",
    val statusFilter: String = "",
    val genderFilter: String = "",
) {
    val filteredCharacters: List<Character>
        get() = characters.filter { character ->
            character.name.contains(searchQuery, ignoreCase = true) &&
            (statusFilter.isEmpty() || character.status.equals(statusFilter, ignoreCase = true)) &&
            (genderFilter.isEmpty() || character.gender.equals(genderFilter, ignoreCase = true)) &&
            (speciesQuery.isEmpty() || character.species.equals(speciesQuery, ignoreCase = true)) &&
            (typeQuery.isEmpty() || character.type.equals(typeQuery, ignoreCase = true))
        }
}