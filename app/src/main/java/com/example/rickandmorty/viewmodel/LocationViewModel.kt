package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Location
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(LocationScreenState())
    var id by mutableIntStateOf(0)

    init {
        viewModelScope.launch {
            val response = repository.getLocationList(state.page)
            state = state.copy(
                locations = response.body()!!.results
            )
        }
    }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    fun getLocation() {
        viewModelScope.launch {
            val response = repository.getLocation(id = id)
            state = state.copy(
                location = response.body()!!
            )
        }
    }
}

data class LocationScreenState(
    val locations: List<Location> = emptyList(),
    val searchQuery: String = "",
    val page: Int = 1,
    val location : Location = Location()
)