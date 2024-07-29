package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rickandmorty.components.main_screen_ui.EpisodeUI
import com.example.rickandmorty.components.navigation_ui.Screen
import com.example.rickandmorty.viewmodel.EpisodeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodeFavorites(
    navController: NavHostController,
    viewModel: EpisodeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    val favoriteEpisodes = state.episodes.filter { episode ->
        state.favoriteEpisodeIds.contains(episode.id.toString())
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(top = 20.dp),
        content = {
            items(favoriteEpisodes.size) { index ->
                val episode = favoriteEpisodes[index]
                EpisodeUI(
                    episode = episode,
                    isFavorite = true,
                    onClick = {
                        navController.navigate(Screen.EpisodeDetails.createRoute(episode.id))
                    },
                    onFavoriteClick = {
                        viewModel.toggleFavoriteEpisode(episode.id.toString())
                    }
                )
            }
        }
    )
}