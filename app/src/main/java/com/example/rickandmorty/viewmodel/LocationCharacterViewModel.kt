package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import com.example.rickandmorty.model.Character
import com.example.rickandmorty.repository.CharacterDownload
import com.example.rickandmorty.util.Resource

class LocationCharacterViewModel(
    private val repository: CharacterDownload
) : ViewModel() {

    var state by mutableStateOf(LocationCharacterScreenState())
    val error = MutableLiveData<Resource<Exception>>()
    val isLoading = MutableLiveData<Resource<Boolean>>()

    fun getCharacters(characterURLs: List<String>) {
        viewModelScope.launch {
            isLoading.value = Resource.loading(true)
            val characterIDs = characterURLs.map { it.substringAfterLast("/").toInt() }
            val deferredCharacters = characterIDs.map { id ->
                async {
                    repository.getCharacter(id)
                }
            }

            isLoading.value = Resource.loading(false)
            state = state.copy(
                characters = deferredCharacters.map { it.await().data!! }
            )
        }
    }
}

data class LocationCharacterScreenState(
    val characters : List<Character> = emptyList(),
)