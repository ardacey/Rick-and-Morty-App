package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.repository.LocationDownload
import com.example.rickandmorty.util.Resource
import kotlinx.coroutines.launch

class LocationDetailsViewModel(
    private val repository: LocationDownload
) : ViewModel() {

    var state by mutableStateOf(LocationDetailsScreenState())
    val error = MutableLiveData<Resource<Exception>>()
    val isLoading = MutableLiveData<Resource<Boolean>>()

    fun getLocation(id : Int) {
        isLoading.value = Resource.loading(true)
        viewModelScope.launch {
            val response = repository.getLocation(id)
            isLoading.value = Resource.loading(false)

            state = state.copy(
                location = response.data!!
            )
        }
    }
}

data class LocationDetailsScreenState(
    val location: Location = Location(),
)