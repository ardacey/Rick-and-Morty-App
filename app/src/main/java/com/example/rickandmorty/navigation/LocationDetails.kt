package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rickandmorty.components.details_screen_ui.CharacterCard
import com.example.rickandmorty.components.LoadingIndicator
import com.example.rickandmorty.components.details_screen_ui.LocationDetailsHeader
import com.example.rickandmorty.components.navigation_ui.Screen
import com.example.rickandmorty.viewmodel.LocationCharacterViewModel
import com.example.rickandmorty.viewmodel.LocationDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LocationDetailsScreen(
    id : Int?, navController: NavController?,
    locationViewModel: LocationDetailsViewModel = koinViewModel(),
    characterViewModel: LocationCharacterViewModel = koinViewModel()) {

    val isCharacterLoading by characterViewModel.isLoading.collectAsState()
    val locationError by locationViewModel.error.collectAsState()
    val characterError by characterViewModel.error.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(locationError, characterError) {
        if (locationError != null || characterError != null) {
            val error = locationError ?: characterError
            error?.let {
                snackbarHostState.showSnackbar(message = it.toString())
                locationViewModel.clearError()
                characterViewModel.clearError()
            }
        }
    }

    LaunchedEffect(id) {
        id?.let { locationViewModel.getLocation(it) }
    }

    val locationState by locationViewModel.state.collectAsState()
    val location = locationState.location

    LaunchedEffect(location.residents) {
        characterViewModel.getCharacters(location.residents)
    }

    val charactersState by characterViewModel.state.collectAsState()
    val characters = charactersState.characters

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            LocationDetailsHeader(location)
            Text(
                text = "Residents (${characters.size})",
                modifier = Modifier.padding(16.dp),
                style = MaterialTheme.typography.titleLarge,
            )
        }
        if (isCharacterLoading) {
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