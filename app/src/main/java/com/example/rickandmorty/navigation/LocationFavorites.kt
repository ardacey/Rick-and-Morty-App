package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.rickandmorty.components.main_screen_ui.LocationUI
import com.example.rickandmorty.components.navigation_ui.Screen
import com.example.rickandmorty.viewmodel.LocationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationFavorites(
    navController: NavHostController,
    viewModel: LocationViewModel = koinViewModel()
) {
    val state by viewModel.state.collectAsState()

    val favoriteLocations = state.locations.filter { location ->
        state.favoriteLocationIds.contains(location.id.toString())
    }

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize().padding(top = 20.dp),
        content = {
            items(favoriteLocations.size) { index ->
                val location = favoriteLocations[index]
                LocationUI(
                    location = location,
                    isFavorite = true,
                    onClick = {
                        navController.navigate(Screen.LocationDetails.createRoute(location.id))
                    },
                    onFavoriteClick = {
                        viewModel.toggleFavoriteLocation(location.id.toString())
                    }
                )
            }
        }
    )
}