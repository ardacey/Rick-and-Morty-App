package com.example.rickandmorty.presentation.ui.episodes_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
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
import androidx.navigation.NavHostController
import com.example.rickandmorty.presentation.ui.episodes_screen.components.EpisodeBottomSheet
import com.example.rickandmorty.presentation.ui.episodes_screen.components.EpisodeUI
import com.example.rickandmorty.presentation.common.LoadingIndicator
import com.example.rickandmorty.presentation.navigation.Screen
import com.example.rickandmorty.presentation.common.SearchBar
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodesScreen(
    navController: NavHostController,
    viewModel: EpisodeViewModel = koinViewModel()
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
            placeholderText = "Search Episodes",
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
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                content = {
                    val filteredEpisodes = state.filteredEpisodes
                    items(filteredEpisodes.size) { index ->
                        val episode = filteredEpisodes[index]
                        val isFavorite = state.favoriteEpisodeIds.contains(episode.id.toString())

                        EpisodeUI(
                            episode = episode,
                            isFavorite = isFavorite,
                            onClick = {
                                navController.navigate(Screen.EpisodeDetails.createRoute(episode.id))
                            },
                            onFavoriteClick = {
                                viewModel.toggleFavoriteEpisode(episode.id.toString())
                            },
                        )
                    }
                }
            )
        }
    }
    if (showBottomSheet.value) {
        EpisodeBottomSheet(onDismissRequest = { showBottomSheet.value = false })
    }
}