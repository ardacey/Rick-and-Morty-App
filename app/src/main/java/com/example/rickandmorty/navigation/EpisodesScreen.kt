package com.example.rickandmorty.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.rickandmorty.components.EpisodeBottomSheet
import com.example.rickandmorty.components.EpisodeUI
import com.example.rickandmorty.components.Screen
import com.example.rickandmorty.components.SearchBar
import com.example.rickandmorty.components.TopBar
import com.example.rickandmorty.viewmodel.EpisodeViewModel
import org.koin.androidx.compose.koinViewModel

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun EpisodesScreen(
    navController: NavHostController,
    viewModel: EpisodeViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val showBottomSheet = remember { mutableStateOf(false) }
    
    Scaffold(
        topBar = { TopBar("Episodes") },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(paddingValues),
            ) {
                SearchBar(
                    searchQuery = state.searchQuery,
                    onValueChange = {viewModel.updateSearchQuery(it)},
                    placeholderText = "Search Episodes",
                    showOptionsSheet = { showBottomSheet.value = true })
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    content = {
                        val filteredEpisodes = state.filteredEpisodes
                        items(filteredEpisodes.size) { index ->
                            EpisodeUI(
                                episode = filteredEpisodes[index]
                            ) { navController.navigate(Screen.EpisodeDetails.createRoute(filteredEpisodes[index].id)) }
                        }
                    }
                )
            }
            if (showBottomSheet.value) {
                EpisodeBottomSheet(onDismissRequest = { showBottomSheet.value = false })
            }
        }
    )
    error?.let {
        viewModel.clearError()
    }
}