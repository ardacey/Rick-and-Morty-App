package com.example.rickandmorty.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
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
import com.example.rickandmorty.components.LocationBottomSheet
import com.example.rickandmorty.components.SearchBar
import com.example.rickandmorty.components.TopBar
import com.example.rickandmorty.model.Location
import com.example.rickandmorty.viewmodel.LocationViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun LocationsScreen(navController: NavHostController) {
    val locationViewModel = koinViewModel<LocationViewModel>()
    val state = locationViewModel.state
    val showBottomSheet = remember { mutableStateOf(false) }

    Scaffold(
        modifier = Modifier.background(Color.Transparent),
        topBar = { TopBar("Locations") },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(paddingValues),
            ) {
                SearchBar(
                    locationViewModel.state.searchQuery,
                    {locationViewModel.updateSearchQuery(it)},
                    "Search Locations",
                    showOptionsSheet = { showBottomSheet.value = true })
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    content = {
                        val filteredLocations = state.filteredLocations
                        items(filteredLocations.size) { index ->
                            LocationUI(
                                location = filteredLocations[index]
                            ) { navController.navigate("Location Details/${filteredLocations[index].id}") }
                        }
                    }
                )
            }
            if (showBottomSheet.value) {
                LocationBottomSheet(onDismissRequest = { showBottomSheet.value = false })
            }
        }
    )
}

@Composable
private fun LocationUI(location: Location, onClick: () -> Unit = { }) {
    Card(
        Modifier
            .size(150.dp)
            .padding(10.dp)
            .clickable { onClick.invoke() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp)
        ) {
            Text(
                text = location.name,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp
            )
            Text(
                text = location.type,
                fontSize = 14.sp
            )
            Text(
                text = location.dimension,
                fontSize = 14.sp
            )
        }
    }
}