package com.example.rickandmorty.presentation.ui.characters_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import com.example.rickandmorty.presentation.ui.characters_screen.components.CharacterBottomSheet
import com.example.rickandmorty.presentation.ui.characters_screen.components.CharacterUI
import com.example.rickandmorty.presentation.common.LoadingIndicator
import com.example.rickandmorty.presentation.navigation.Screen
import com.example.rickandmorty.presentation.common.SearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharactersScreen(
    navController: NavHostController,
    viewModel: CharacterViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val error by viewModel.error.collectAsState()
    val showBottomSheet = remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            searchQuery = state.searchQuery,
            onValueChange = viewModel::updateSearchQuery,
            placeholderText = "Search Characters",
            showOptionsSheet = { showBottomSheet.value = true },
            clearSearch = { viewModel.updateSearchQuery("") }
        )
        if (error != null) {
            Text(
                text = error!!,
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.displayMedium,
                textAlign = TextAlign.Center,
                color = Color.Red
            )
        } else if (state.loading) {
            LoadingIndicator()
        } else {
            LazyColumn(
                Modifier.fillMaxSize(),
                content = {
                    val filteredCharacters = state.filteredCharacters
                    items(filteredCharacters.size) { index ->
                        val character = filteredCharacters[index]
                        val isFavorite = state.favoriteCharacterIds.contains(character.id.toString())

                        CharacterUI(
                            character = character,
                            isFavorite = isFavorite,
                            onClick = {
                                navController.navigate(Screen.CharacterDetails.createRoute(character.id)) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        inclusive = true
                                    }
                                }
                            },
                            onFavoriteClick = {
                                viewModel.toggleFavoriteCharacter(character.id.toString())
                            }
                        )
                    }
                }
            )
        }
    }
    if (showBottomSheet.value) {
        CharacterBottomSheet(onDismissRequest = { showBottomSheet.value = false })
    }
}