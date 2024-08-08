package com.example.rickandmorty.presentation.ui.characters_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.presentation.common.FilterCheckbox
import com.example.rickandmorty.presentation.common.SearchFilter
import com.example.rickandmorty.presentation.ui.characters_screen.CharacterViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterBottomSheet(onDismissRequest: () -> Unit, viewModel: CharacterViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val speciesSuggestions by viewModel.speciesSuggestions.collectAsState()
    val typeSuggestions by viewModel.typeSuggestions.collectAsState()

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column {
            SearchFilter(
                searchQuery = state.speciesQuery,
                onValueChange = viewModel::updateSpeciesQuery,
                suggestions = speciesSuggestions,
                onSuggestionSelected = viewModel::updateSpeciesSuggestions,
                clearSearch = { viewModel.updateSpeciesQuery("") },
                placeholderText = "Species"
            )

            SearchFilter(
                searchQuery = state.typeQuery,
                onValueChange = viewModel::updateTypeQuery,
                suggestions = typeSuggestions,
                onSuggestionSelected = viewModel::updateTypeSuggestions,
                clearSearch = { viewModel.updateTypeQuery("") },
                placeholderText = "Type"
            )

            FilterCheckbox(
                label = "Only Favorites",
                isSelected = state.onlyFavorites,
                onCheckedChange = viewModel::updateOnlyFavorites
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Status",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.displayMedium
            )

            FilterCheckbox(
                label = "Alive",
                isSelected = state.statusFilter == "Alive",
                onCheckedChange = {
                    viewModel.updateStatusFilter(if (it) "Alive" else "")
                }
            )

            FilterCheckbox(
                label = "Dead",
                isSelected = state.statusFilter == "Dead",
                onCheckedChange = {
                    viewModel.updateStatusFilter(if (it) "Dead" else "")
                }
            )

            FilterCheckbox(
                label = "Unknown",
                isSelected = state.statusFilter == "unknown",
                onCheckedChange = {
                    viewModel.updateStatusFilter(if (it) "unknown" else "")
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Gender",
                modifier = Modifier.padding(horizontal = 20.dp),
                style = MaterialTheme.typography.displayMedium
            )

            FilterCheckbox(
                label = "Male",
                isSelected = state.genderFilter == "Male",
                onCheckedChange = {
                    viewModel.updateGenderFilter(if (it) "Male" else "")
                }
            )

            FilterCheckbox(
                label = "Female",
                isSelected = state.genderFilter == "Female",
                onCheckedChange = {
                    viewModel.updateGenderFilter(if (it) "Female" else "")
                }
            )

            FilterCheckbox(
                label = "Genderless",
                isSelected = state.genderFilter == "Genderless",
                onCheckedChange = {
                    viewModel.updateGenderFilter(if (it) "Genderless" else "")
                }
            )

            FilterCheckbox(
                label = "Unknown",
                isSelected = state.genderFilter == "unknown",
                onCheckedChange = {
                    viewModel.updateGenderFilter(if (it) "unknown" else "")
                }
            )
        }
    }
}