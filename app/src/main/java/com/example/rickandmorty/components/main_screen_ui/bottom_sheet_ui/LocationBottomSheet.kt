package com.example.rickandmorty.components.main_screen_ui.bottom_sheet_ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.viewmodel.LocationViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationBottomSheet(onDismissRequest: () -> Unit, viewModel: LocationViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    val typeSuggestions by viewModel.typeSuggestions.collectAsState()
    val dimensionSuggestions by viewModel.dimensionSuggestions.collectAsState()

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column {
            Text(text = "Type:", modifier = Modifier.padding(horizontal = 20.dp))
            SearchFilter(
                searchQuery = state.typeQuery,
                onValueChange = { viewModel.updateTypeQuery(it) },
                suggestions = typeSuggestions,
                onSuggestionSelected = { viewModel.updateTypeSuggestions(it) }
            )

            Text(text = "Dimension:", modifier = Modifier.padding(horizontal = 20.dp))
            SearchFilter(
                searchQuery = state.dimensionQuery,
                onValueChange = { viewModel.updateDimensionQuery(it) },
                suggestions = dimensionSuggestions,
                onSuggestionSelected = { viewModel.updateDimensionSuggestions(it) }
            )
        }
    }
}