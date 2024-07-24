package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.repository.CharacterDownload
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val repository: CharacterDownload
) : ViewModel() {

    var state by mutableStateOf(CharacterScreenState())
    val error = MutableLiveData<Resource<Exception>>()
    val isLoading = MutableLiveData<Resource<Boolean>>()

    init { getCharacter() }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    fun updateStatusFilter(filter: String) {
        state = state.copy(statusFilter = filter)
    }

    fun updateGenderFilter(filter: String) {
        state = state.copy(genderFilter = filter)
    }

    private fun getCharacter() {
        isLoading.value = Resource.loading(true)
        viewModelScope.launch {
            val firstPageResponse = repository.getCharacterList(1)
            val totalPages = firstPageResponse.data?.info?.pages ?: 1

            val deferredList = (1..totalPages).map { currentPage ->
                async {
                    val response = repository.getCharacterList(currentPage)
                    response.data?.results.orEmpty()
                }
            }

            isLoading.value = Resource.loading(false)
            val characterList = deferredList.awaitAll().flatten()
            state = state.copy(characters = characterList)
        }
    }
}

data class CharacterScreenState(
    val characters: List<Character> = emptyList(),
    val searchQuery: String = "",
    val statusFilter: String = "",
    val genderFilter: String = "",
) {
    val filteredCharacters: List<Character>
        get() = characters.filter { character ->
            character.name.contains(searchQuery, ignoreCase = true) &&
            (statusFilter.isEmpty() || character.status.equals(statusFilter, ignoreCase = true)) &&
            (genderFilter.isEmpty() || character.gender.equals(genderFilter, ignoreCase = true))
        }
}