package com.example.rickandmorty.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.rickandmorty.model.Character
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.launch

class CharacterViewModel : ViewModel() {
    private val repository = Repository()
    var state by mutableStateOf(CharacterScreenState())

    /*private val pagination = PaginationFactory(
        initialPage = state.page,
        onLoadUpdated = { isLoading ->
            state = state.copy(isLoading = isLoading)
        },
        onRequest = { page ->
            repository.getCharacterList(page)
        },
        getNextPage = { state.page + 1 },
        onError = {
            state = state.copy(error = it?.localizedMessage)
        },
        onSuccess = { items, newPage ->
            state = state.copy(
                characters = state.characters + items.results,
                page = newPage,
                endOfPaginationReached = state.page == items.info.pages
            )
        }
    )

    init { loadNextPage() }

    fun loadNextPage() {
        viewModelScope.launch {
            pagination.loadNextItems()
        }
    }*/

    init { getCharacter() }

    fun updateSearchQuery(query: String) {
        state = state.copy(searchQuery = query)
    }

    private fun getCharacter() {
        viewModelScope.launch {
            val allCharacters = mutableListOf<Character>()
            val firstPageResponse = repository.getCharacterList(1)
            val totalPages = firstPageResponse.body()?.info?.pages ?: 1

            val deferredList = (1..totalPages).map { currentPage ->
                async {
                    val response = repository.getCharacterList(currentPage)
                    response.body()?.results.orEmpty()
                }
            }

            val characterList = deferredList.awaitAll().flatten()
            allCharacters.addAll(characterList)
            state = state.copy(characters = allCharacters)
        }
    }
}

data class CharacterScreenState(
    val characters: List<Character> = emptyList(),
    val searchQuery: String = "",
    /*val page: Int = 1,
    val endOfPaginationReached: Boolean = false,
    val error: String? = null,
    val isLoading: Boolean = false,*/
)