package com.example.rickandmorty.presentation.ui.location_details_screen

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
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
import com.example.rickandmorty.presentation.common.CharacterCard
import com.example.rickandmorty.presentation.common.LoadingIndicator
import com.example.rickandmorty.presentation.ui.location_details_screen.components.LocationDetailsHeader
import com.example.rickandmorty.presentation.navigation.Screen
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun LocationDetailsScreen(
    id : Int?,
    navController: NavController,
) {

    val viewModel = koinViewModel<LocationDetailsViewModel> {
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
            } else { LocationDetailsHeader(state.location, navController) }
            Text(
                text = "Residents (${state.characters.size})",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        if (state.loading) {
            item { LoadingIndicator() }
        } else {
            item {
                val gridRows = state.characters.chunked(3)
                gridRows.forEach { row ->
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 8.dp),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        row.forEach { character ->
                            CharacterCard(
                                character = character,
                                onClick = {
                                    navController.navigate(
                                        Screen.CharacterDetails.createRoute(character.id)
                                    ) {
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}