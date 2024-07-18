package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Location
import kotlinx.coroutines.launch

class LocationViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(LocationScreenState())

    init { getResponse(state.page) }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    private fun getResponse(page : Int) {
        viewModelScope.launch {
            val response = repository.getLocationList(state.page)
            state = state.copy(
                locations = response.body()!!.results
            )
        }
    }
}

data class LocationScreenState(
    val locations: List<Location> = emptyList(),
    val searchQuery: String = "",
    val page: Int = 1,
)