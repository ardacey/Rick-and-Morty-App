package com.example.rickandmorty.components.main_screen_ui.bottom_sheet_ui

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.viewmodel.episode.EpisodeViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeBottomSheet(onDismissRequest: () -> Unit, viewModel: EpisodeViewModel = koinViewModel()) {
    val dateState = rememberDateRangePickerState()
    val startDateMillis by rememberUpdatedState(dateState.selectedStartDateMillis)
    val endDateMillis by rememberUpdatedState(dateState.selectedEndDateMillis)
    val state by viewModel.state.collectAsState()

    LaunchedEffect(startDateMillis, endDateMillis) {
        viewModel.updateStartDate(startDateMillis?.toLocalDate() ?: LocalDate.MIN)
        viewModel.updateEndDate(endDateMillis?.toLocalDate() ?: LocalDate.MAX)
    }

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        FilterCheckbox(
            label = "Only Favorites",
            isSelected = state.onlyFavorites,
            onCheckedChange = viewModel::updateOnlyFavorites
        )

        Spacer(modifier = Modifier.height(16.dp))

        DateRangePicker(state = dateState)
    }
}

private fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}