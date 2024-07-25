package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rickandmorty.components.CharacterCard
import com.example.rickandmorty.components.Screen
import com.example.rickandmorty.viewmodel.EpisodeCharacterViewModel
import com.example.rickandmorty.viewmodel.EpisodeDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun EpisodeDetailsScreen(
    id : Int?, navController: NavController?,
    episodeViewModel: EpisodeDetailsViewModel = koinViewModel(),
    characterViewModel: EpisodeCharacterViewModel = koinViewModel()) {

    LaunchedEffect(id) {
        id?.let { episodeViewModel.getEpisode(it) }
    }

    val episodeState by episodeViewModel.state.collectAsState()
    val episode = episodeState.episode

    LaunchedEffect(episode.characters) {
        characterViewModel.getCharacters(episode.characters)
    }

    val charactersState by characterViewModel.state.collectAsState()
    val characters = charactersState.characters

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Text(
                    text = episode.episode,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = episode.name,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                )
                Text(
                    text = episode.airDate,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 16.sp,
                )
            }
            Text(
                text = "Characters (${characters.size})",
                modifier = Modifier.padding(16.dp),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
        }
        item {
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                maxItemsInEachRow = 3,
                horizontalArrangement = Arrangement.SpaceAround,
            ) {
                characters.forEach { character ->
                    CharacterCard(
                        character,
                        onClick = { navController?.navigate(Screen.CharacterDetails.createRoute(character.id)) }
                    )
                }
            }
        }
    }
}