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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.rickandmorty.components.CharacterCard
import com.example.rickandmorty.components.Screen
import com.example.rickandmorty.viewmodel.LocationCharacterViewModel
import com.example.rickandmorty.viewmodel.LocationDetailsViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun LocationDetailsScreen(
    id : Int?, navController: NavController?,
    locationViewModel: LocationDetailsViewModel = koinViewModel(),
    characterViewModel: LocationCharacterViewModel = koinViewModel()) {

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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                Text(
                    text = location.name,
                    modifier = Modifier.padding(16.dp),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = location.type,
                    fontSize = 16.sp,
                )
                Text(
                    text = location.dimension,
                    fontSize = 16.sp,
                )
            }
            Text(
                text = "Residents (${characters.size})",
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