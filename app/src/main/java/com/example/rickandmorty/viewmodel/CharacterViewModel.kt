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

    init { getResponse(state.page) }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    private fun getResponse(page : Int) {
        viewModelScope.launch {
            val response = repository.getCharacterList(page)
            state = state.copy(
                characters = response.body()!!.results
            )
        }
    }
}

data class CharacterScreenState(
    val characters: List<Character> = emptyList(),
    val searchQuery: String = "",
    val page: Int = 1,
)