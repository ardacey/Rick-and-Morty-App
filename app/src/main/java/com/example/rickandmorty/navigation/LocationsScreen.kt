package com.example.rickandmorty.navigation

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
import com.example.rickandmorty.components.LoadingIndicator
import com.example.rickandmorty.components.LocationBottomSheet
import com.example.rickandmorty.components.LocationUI
import com.example.rickandmorty.components.Screen
import com.example.rickandmorty.components.SearchBar
import com.example.rickandmorty.viewmodel.LocationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationsScreen(
    navController: NavHostController,
    viewModel: LocationViewModel = koinViewModel()
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
            state.searchQuery,
            {viewModel.updateSearchQuery(it)},
            "Search Locations",
            showOptionsSheet = { showBottomSheet.value = true })
        if (isLoading) {
            LoadingIndicator()
        } else {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                content = {
                    val filteredLocations = state.filteredLocations
                    items(filteredLocations.size) { index ->
                        LocationUI(
                            location = filteredLocations[index]
                        ) {
                            navController.navigate(
                                Screen.LocationDetails.createRoute(
                                    filteredLocations[index].id
                                )
                            )
                        }
                    }
                }
            )
        }
    }
    if (showBottomSheet.value) {
        LocationBottomSheet(onDismissRequest = { showBottomSheet.value = false })
    }
}