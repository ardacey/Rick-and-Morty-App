package com.example.rickandmorty.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.viewmodel.EpisodeViewModel

@Composable
fun EpisodesScreen(navController: NavHostController) {
    val episodeViewModel = viewModel<EpisodeViewModel>()
    val state = episodeViewModel.state
    
    Scaffold(
        modifier = Modifier.background(Color.Transparent),
        topBar = { TopBar("Episodes") },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(paddingValues),
            ) {
                SearchBar(
                    episodeViewModel.state.searchQuery,
                    {episodeViewModel.updateSearchQuery(it)},
                    "Search Episodes")
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    content = {
                        val filteredEpisodes = state.episodes.filter { episode ->
                            episode.name.contains(state.searchQuery, ignoreCase = true)
                        }
                        items(filteredEpisodes.size) { index ->
                            EpisodeUI(
                                episode = filteredEpisodes[index]
                            ) { navController.navigate("Episode Details/${filteredEpisodes[index].id}") }
                        }
                    }
                )
            }
        }
    )
}

@Composable
private fun EpisodeUI(episode: Episode, onClick: () -> Unit = { }) {
    Card(
        Modifier
            .wrapContentSize()
            .padding(10.dp)
            .clickable { onClick.invoke() },
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            Text(
                text = episode.episode + ": " + episode.name,
                modifier = Modifier.padding(horizontal = 6.dp),
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Air Date",
                modifier = Modifier.padding(horizontal = 6.dp),
                fontSize = 10.sp
            )
            Text(
                text = episode.airDate,
                modifier = Modifier.padding(horizontal = 6.dp),
                fontSize = 12.sp
            )
        }
    }

}