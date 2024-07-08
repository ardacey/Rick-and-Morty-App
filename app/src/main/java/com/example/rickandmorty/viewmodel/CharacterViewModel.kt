package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(CharacterScreenState())
    init {
        viewModelScope.launch {
            val response = repository.getCharacterList()
            state = state.copy(
                characters = response.body()!!.results
            )
        }
    }
    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }
}

data class CharacterScreenState(
    val characters: List<Character> = emptyList(),
    val searchQuery: String = ""
)