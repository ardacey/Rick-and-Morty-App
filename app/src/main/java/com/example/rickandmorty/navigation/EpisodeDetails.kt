package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import com.example.rickandmorty.components.details_screen_ui.CharacterCard
import com.example.rickandmorty.components.LoadingIndicator
import com.example.rickandmorty.components.details_screen_ui.EpisodeDetailsHeader
import com.example.rickandmorty.components.navigation_ui.Screen
import com.example.rickandmorty.viewmodel.EpisodeCharacterViewModel
import com.example.rickandmorty.viewmodel.EpisodeDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EpisodeDetailsScreen(
    id : Int?, navController: NavController?,
    episodeViewModel: EpisodeDetailsViewModel = koinViewModel(),
    characterViewModel: EpisodeCharacterViewModel = koinViewModel()) {

    val isCharacterLoading by characterViewModel.isLoading.collectAsState()
    val episodeState by episodeViewModel.state.collectAsState()
    val episodeError by episodeViewModel.error.collectAsState()
    val charactersState by characterViewModel.state.collectAsState()
    val characterError by characterViewModel.error.collectAsState()

    LaunchedEffect(id) {
        id?.let { episodeViewModel.getEpisode(it) }
    }

    val episode = episodeState.episode

    LaunchedEffect(episode.characters) {
        characterViewModel.getCharacters(episode.characters)
    }

    val characters = charactersState.characters

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (episodeError != null) {
                Text(
                    text = episodeError!!,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Red
                )
            } else { EpisodeDetailsHeader(episode) }
            Text(
                text = "Characters (${characters.size})",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        if (characterError != null) {
            item {
                Text(
                    text = characterError!!,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Red
                )
            }
        } else if (isCharacterLoading) {
            item { LoadingIndicator() }
        }
        else {
            item {
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    maxItemsInEachRow = 3,
                    horizontalArrangement = Arrangement.SpaceAround,
                ) {
                    characters.forEach { character ->
                        CharacterCard(
                            character,
                            onClick = {
                                navController?.navigate(
                                    Screen.CharacterDetails.createRoute(character.id)
                                )
                            }
                        )
                    }
                }
            }
        }
    }
}