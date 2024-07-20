package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.example.rickandmorty.model.Character

class LocationCharacterViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(LocationCharacterScreenState())

    fun getCharacters(characterURLs: List<String>) {
        viewModelScope.launch {
            val characterIDs = characterURLs.map { it.substringAfterLast("/").toInt() }
            val deferredCharacters = characterIDs.map { id ->
                async {
                    repository.getCharacter(id)
                }
            }

            state = state.copy(
                characters = deferredCharacters.map { it.await().body()!! }
            )
        }
    }
}

data class LocationCharacterScreenState(
    val characters : List<Character> = emptyList(),
)