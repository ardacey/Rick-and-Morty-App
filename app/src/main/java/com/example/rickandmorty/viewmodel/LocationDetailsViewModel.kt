package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Location
import kotlinx.coroutines.launch

class LocationDetailsViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(LocationDetailsScreenState())

    fun getLocation(id : Int) {
        viewModelScope.launch {
            val response = repository.getLocation(id)
            state = state.copy(
                location = response.body()!!
            )
        }
    }
}

data class LocationDetailsScreenState(
    val location: Location = Location(),
)