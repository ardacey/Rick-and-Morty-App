package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(CharacterScreenState())
    var id by mutableIntStateOf(0)

    init {
        viewModelScope.launch {
            val response = repository.getCharacterList(state.page)
            state = state.copy(
                characters = response.body()!!.results
            )
        }
    }
    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }
    fun getCharacter() {
        viewModelScope.launch {
            val response = repository.getCharacter(id = id)
            state = state.copy(
                character = response.body()!!
            )
        }
    }
}

data class CharacterScreenState(
    val characters: List<Character> = emptyList(),
    val searchQuery: String = "",
    val page: Int = 1,
    val character : Character = Character()
)