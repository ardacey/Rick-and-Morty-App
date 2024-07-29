package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rickandmorty.components.details_screen_ui.EpisodeCard
import com.example.rickandmorty.components.LoadingIndicator
import com.example.rickandmorty.components.details_screen_ui.CharacterDetailsHeader
import com.example.rickandmorty.components.navigation_ui.Screen
import com.example.rickandmorty.viewmodel.CharacterDetailsViewModel
import com.example.rickandmorty.viewmodel.CharacterEpisodeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetailsScreen(
    id : Int?,
    navController: NavController?,
    characterViewModel: CharacterDetailsViewModel = koinViewModel(),
    episodeViewModel: CharacterEpisodeViewModel = koinViewModel()) {

    val isEpisodeLoading by episodeViewModel.isLoading.collectAsState()
    val characterState by characterViewModel.state.collectAsState()
    val episodesState by episodeViewModel.state.collectAsState()
    val characterError by characterViewModel.error.collectAsState()
    val episodeError by episodeViewModel.error.collectAsState()

    LaunchedEffect(id) {
        id?.let { characterViewModel.getCharacter(it) }
    }

    val character = characterState.character

    LaunchedEffect(character.episode) {
        episodeViewModel.getEpisodes(character.episode)
    }

    val episodes = episodesState.episodes

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (characterError != null) {
                Text(
                    text = characterError!!,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Red
                )
            } else { CharacterDetailsHeader(character) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Episodes (${character.episode.size})",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(12.dp),
                )
            }
        }
        if (episodeError != null) {
            item {
                Text(
                    text = episodeError!!,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Red
                )
            }
        } else if (isEpisodeLoading) {
            item { LoadingIndicator() }
        } else {
            items(episodes.size) { index ->
                EpisodeCard(
                    episodes[index]
                ) {
                    navController?.navigate(
                        Screen.EpisodeDetails.createRoute(episodes[index].id))
                }
            }
        }
    }
}