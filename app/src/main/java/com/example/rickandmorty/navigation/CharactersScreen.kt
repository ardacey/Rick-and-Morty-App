package com.example.rickandmorty.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavHostController
import com.example.rickandmorty.components.CharacterBottomSheet
import com.example.rickandmorty.components.CharacterUI
import com.example.rickandmorty.components.Screen
import com.example.rickandmorty.components.SearchBar
import com.example.rickandmorty.components.TopBar
import com.example.rickandmorty.viewmodel.CharacterViewModel
import org.koin.androidx.compose.koinViewModel

@Composable
fun CharactersScreen(navController: NavHostController, viewModel: CharacterViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val error by viewModel.error.collectAsState()
    val showBottomSheet = remember { mutableStateOf(false) }

    Scaffold(
        topBar = { TopBar("Characters") },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Transparent)
                    .padding(paddingValues),
            ) {
                SearchBar(
                    state.searchQuery,
                    {viewModel.updateSearchQuery(it)},
                    "Search Characters",
                    showOptionsSheet = { showBottomSheet.value = true })
                LazyColumn(
                    Modifier.fillMaxSize(),
                    content = {
                        val filteredCharacters = state.filteredCharacters
                        items(filteredCharacters.size) { index ->
                            CharacterUI(
                                character = filteredCharacters[index]
                            ) { navController.navigate(Screen.CharacterDetails.createRoute(filteredCharacters[index].id)) }
                        }
                    }
                )
            }
            if (showBottomSheet.value) {
                CharacterBottomSheet(onDismissRequest = { showBottomSheet.value = false })
            }
        }
    )
    error?.let {
        viewModel.clearError()
    }
}