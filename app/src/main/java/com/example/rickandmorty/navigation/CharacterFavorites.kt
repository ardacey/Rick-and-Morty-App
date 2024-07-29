package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rickandmorty.components.main_screen_ui.CharacterUI
import com.example.rickandmorty.components.navigation_ui.Screen
import com.example.rickandmorty.viewmodel.CharacterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterFavorites(
    navController: NavHostController,
    viewModel: CharacterViewModel = koinViewModel()
){
    val state by viewModel.state.collectAsState()

    val favoriteCharacters = state.characters.filter { character ->
        state.favoriteCharacterIds.contains(character.id.toString())
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(top = 20.dp)
    ) {
        items(favoriteCharacters.size) { index ->
            val character = favoriteCharacters[index]
            CharacterUI(
                character = character,
                isFavorite = true,
                onClick = {
                    navController.navigate(Screen.CharacterDetails.createRoute(character.id))
                },
                onFavoriteClick = {
                    viewModel.toggleFavoriteCharacter(character.id.toString())
                }
            )
        }
    }
}