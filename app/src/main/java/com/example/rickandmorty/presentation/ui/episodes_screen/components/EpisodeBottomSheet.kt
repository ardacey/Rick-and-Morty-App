package com.example.rickandmorty.presentation.ui.episodes_screen.components

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.rickandmorty.presentation.common.FilterCheckbox
import com.example.rickandmorty.presentation.ui.episodes_screen.EpisodeViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeBottomSheet(onDismissRequest: () -> Unit, viewModel: EpisodeViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        FilterCheckbox(
            label = "Only Favorites",
            isSelected = state.onlyFavorites,
            onCheckedChange = viewModel::updateOnlyFavorites
        )
    }
}