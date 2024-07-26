package com.example.rickandmorty.navigation

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import com.example.rickandmorty.components.main_screen_ui.bottom_sheet_ui.EpisodeBottomSheet
import com.example.rickandmorty.components.main_screen_ui.EpisodeUI
import com.example.rickandmorty.components.LoadingIndicator
import com.example.rickandmorty.components.navigation_ui.Screen
import com.example.rickandmorty.components.main_screen_ui.SearchBar
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
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(error) {
        error?.let {
            snackbarHostState.showSnackbar(message = it.toString())
            viewModel.clearError()
        }
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {
        SearchBar(
            searchQuery = state.searchQuery,
            onValueChange = {viewModel.updateSearchQuery(it)},
            placeholderText = "Search Episodes",
            showOptionsSheet = { showBottomSheet.value = true })
        if (isLoading) {
            LoadingIndicator()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                content = {
                    val filteredEpisodes = state.filteredEpisodes
                    items(filteredEpisodes.size) { index ->
                        EpisodeUI(
                            episode = filteredEpisodes[index]
                        ) {
                            navController.navigate(
                                Screen.EpisodeDetails.createRoute(
                                    filteredEpisodes[index].id
                                )
                            )
                        }
                    }
                }
            )
        }
    }
    if (showBottomSheet.value) {
        EpisodeBottomSheet(onDismissRequest = { showBottomSheet.value = false })
    }
}