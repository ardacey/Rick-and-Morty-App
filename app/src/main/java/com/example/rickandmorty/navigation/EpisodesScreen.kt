package com.example.rickandmorty.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.rickandmorty.components.EpisodeBottomSheet
import com.example.rickandmorty.components.SearchBar
import com.example.rickandmorty.components.TopBar
import com.example.rickandmorty.model.Episode
import com.example.rickandmorty.viewmodel.EpisodeViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun EpisodesScreen(navController: NavHostController) {
    val episodeViewModel = koinViewModel<EpisodeViewModel>()
    val state = episodeViewModel.state
    val showBottomSheet = remember { mutableStateOf(false) }
    
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
                    "Search Episodes",
                    showOptionsSheet = { showBottomSheet.value = true })
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    content = {
                        val filteredEpisodes = state.filteredEpisodes
                        items(filteredEpisodes.size) { index ->
                            EpisodeUI(
                                episode = filteredEpisodes[index]
                            ) { navController.navigate("Episode Details/${filteredEpisodes[index].id}") }
                        }
                    }
                )
            }
            if (showBottomSheet.value) {
                EpisodeBottomSheet(onDismissRequest = { showBottomSheet.value = false })
            }
        }
    )
}

@Composable
private fun EpisodeUI(episode: Episode, onClick: () -> Unit = { }) {
    Card(
        Modifier
            .size(150.dp)
            .padding(10.dp)
            .clickable { onClick.invoke() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(10.dp)
        ) {
            Text(
                text = episode.episode + ": " + episode.name,
                fontWeight = FontWeight.Bold
            )
            Text(
                text = "Air Date: " + episode.airDate,
                fontSize = 12.sp
            )
        }
    }
}