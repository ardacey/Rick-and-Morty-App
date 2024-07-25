package com.example.rickandmorty.components

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.material3.DateRangePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.rememberDateRangePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import com.example.rickandmorty.viewmodel.EpisodeViewModel
import org.koin.androidx.compose.koinViewModel
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EpisodeBottomSheet(onDismissRequest: () -> Unit, viewModel: EpisodeViewModel = koinViewModel()) {
    val dateState = rememberDateRangePickerState()
    val startDateMillis by rememberUpdatedState(dateState.selectedStartDateMillis)
    val endDateMillis by rememberUpdatedState(dateState.selectedEndDateMillis)

    LaunchedEffect(startDateMillis, endDateMillis) {
        viewModel.updateStartDate(startDateMillis?.toLocalDate() ?: LocalDate.MIN)
        viewModel.updateEndDate(endDateMillis?.toLocalDate() ?: LocalDate.MAX)
    }

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        DateRangePicker(state = dateState)
    }
}

@RequiresApi(Build.VERSION_CODES.O)
private fun Long.toLocalDate(): LocalDate {
    return Instant.ofEpochMilli(this).atZone(ZoneId.systemDefault()).toLocalDate()
}