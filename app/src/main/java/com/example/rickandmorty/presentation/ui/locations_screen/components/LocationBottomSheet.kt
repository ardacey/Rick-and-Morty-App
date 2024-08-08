package com.example.rickandmorty.presentation.ui.locations_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import com.example.rickandmorty.presentation.common.FilterCheckbox
import com.example.rickandmorty.presentation.common.SearchFilter
import com.example.rickandmorty.presentation.ui.locations_screen.LocationViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBottomSheet(onDismissRequest: () -> Unit, viewModel: LocationViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val typeSuggestions by viewModel.typeSuggestions.collectAsState()
    val dimensionSuggestions by viewModel.dimensionSuggestions.collectAsState()

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column {
            SearchFilter(
                searchQuery = state.typeQuery,
                onValueChange = viewModel::updateTypeQuery,
                suggestions = typeSuggestions,
                onSuggestionSelected = viewModel::updateTypeSuggestions,
                clearSearch = { viewModel.updateTypeQuery("") },
                placeholderText = "Type"
            )

            SearchFilter(
                searchQuery = state.dimensionQuery,
                onValueChange = viewModel::updateDimensionQuery,
                suggestions = dimensionSuggestions,
                onSuggestionSelected = viewModel::updateDimensionSuggestions,
                clearSearch = { viewModel.updateDimensionQuery("") },
                placeholderText = "Dimension"
            )

            FilterCheckbox(
                label = "Only Favorites",
                isSelected = state.onlyFavorites,
                onCheckedChange = viewModel::updateOnlyFavorites
            )
        }
    }
}