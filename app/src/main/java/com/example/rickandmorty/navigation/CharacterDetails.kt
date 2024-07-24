package com.example.rickandmorty.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import androidx.navigation.NavController
import com.example.rickandmorty.viewmodel.CharacterDetailsViewModel
import com.example.rickandmorty.viewmodel.CharacterEpisodeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharacterDetailsScreen(id : Int?, navController: NavController?) {
    val characterDetailsViewModel = koinViewModel<CharacterDetailsViewModel>()
    characterDetailsViewModel.getCharacter(id!!)
    val character = characterDetailsViewModel.state.character

    val characterEpisodeViewModel = koinViewModel<CharacterEpisodeViewModel>()
    characterEpisodeViewModel.getEpisodes(character.episode)
    val episodes = characterEpisodeViewModel.state.episodes

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(300.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF03A9F4),
                                Color(0xFFFFFF00)
                            ),
                        )
                    ),
                contentAlignment = Alignment.Center,
            ) {
                AsyncImage(
                    model = character.image,
                    contentDescription = character.name,
                    modifier = Modifier
                        .size(150.dp)
                        .clip(CircleShape),
                )
            }
            Text(
                text = character.name,
                modifier = Modifier.padding(16.dp),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = character.status,
                fontSize = 16.sp,
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceAround,

            ) {
                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = character.species,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Species",
                        fontSize = 12.sp,
                    )
                }

                Column (horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = character.gender,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                    )
                    Text(
                        text = "Gender",
                        fontSize = 12.sp,
                    )
                }
            }
            Column(
                horizontalAlignment = Alignment.Start,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)) {
                Text(
                    text = character.location.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                )
                Text(
                    text = "Location",
                    fontSize = 12.sp,
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                horizontalAlignment = Alignment.Start,
            ) {
                Text(
                    text = "Episodes (${character.episode.size})",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(12.dp),
                )
            }
        }
        items(episodes.size) { index ->
            EpisodeCard(
                episodes[index]
            ) { navController?.navigate("Episode Details/${episodes[index].id}") }
        }
    }
}