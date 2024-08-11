package com.example.rickandmorty.presentation.ui.character_details_screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rickandmorty.presentation.ui.character_details_screen.components.EpisodeCard
import com.example.rickandmorty.presentation.common.LoadingIndicator
import com.example.rickandmorty.presentation.ui.character_details_screen.components.CharacterDetailsHeader
import com.example.rickandmorty.presentation.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun CharacterDetailsScreen(
    id : Int?,
    navController: NavController
) {

    val viewModel = koinViewModel<CharacterDetailsViewModel>{
        parametersOf(id)
    }
    val state by viewModel.state.collectAsState()
    val error by viewModel.error.collectAsState()

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            if (error != null) {
                Text(
                    text = error!!,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 48.dp),
                    style = MaterialTheme.typography.displayMedium,
                    color = Color.Red
                )
            } else { CharacterDetailsHeader(state.character, navController) }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Episodes (${state.episodes.size})",
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(12.dp),
                )
            }
        }
        if (state.loading) {
            item { LoadingIndicator() }
        } else {
            items(state.episodes.size) { index ->
                EpisodeCard(
                    state.episodes[index]
                ) {
                    navController.navigate(Screen.EpisodeDetails.createRoute(state.episodes[index].id)) {
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            }
        }
    }
}