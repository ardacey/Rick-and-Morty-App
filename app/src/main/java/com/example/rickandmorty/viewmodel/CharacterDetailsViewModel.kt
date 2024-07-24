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
import kotlinx.coroutines.launch

class CharacterDetailsViewModel(
    private val repository: CharacterDownload
) : ViewModel() {

    var state by mutableStateOf(CharacterDetailsScreenState())
    val error = MutableLiveData<Resource<Exception>>()
    val isLoading = MutableLiveData<Resource<Boolean>>()

    fun getCharacter(id : Int) {
        isLoading.value = Resource.loading(true)
        viewModelScope.launch {
            val response = repository.getCharacter(id)
            isLoading.value = Resource.loading(false)

            state = state.copy(
                character = response.data!!
            )
        }
    }
}

data class CharacterDetailsScreenState(
    val character: Character = Character(),
)