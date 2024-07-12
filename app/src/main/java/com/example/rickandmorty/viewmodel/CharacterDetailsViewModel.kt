package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import kotlinx.coroutines.launch

class CharacterDetailsViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(CharacterDetailsScreenState())

    fun getCharacter(id : Int) {
        viewModelScope.launch {
            val response = repository.getCharacter(id)
            state = state.copy(
                character = response.body()!!
            )
        }
    }
}

data class CharacterDetailsScreenState(
    val character: Character = Character(),
)