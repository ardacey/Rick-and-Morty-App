package com.example.rickandmorty.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.viewmodel.CharacterViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CharacterBottomSheet(onDismissRequest: () -> Unit) {
    val characterViewModel = koinViewModel<CharacterViewModel>()
    val state = characterViewModel.state

    ModalBottomSheet(onDismissRequest = onDismissRequest) {
        Column {
            Text(text = "Status:", modifier = Modifier.padding(horizontal = 20.dp))
            FilterCheckbox(
                label = "Alive",
                isSelected = state.statusFilter == "Alive",
                onCheckedChange = {
                    characterViewModel.updateStatusFilter(if (it) "Alive" else "")
                }
            )
            FilterCheckbox(
                label = "Dead",
                isSelected = state.statusFilter == "Dead",
                onCheckedChange = {
                    characterViewModel.updateStatusFilter(if (it) "Dead" else "")
                }
            )
            FilterCheckbox(
                label = "Unknown",
                isSelected = state.statusFilter == "unknown",
                onCheckedChange = {
                    characterViewModel.updateStatusFilter(if (it) "unknown" else "")
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Gender:", modifier = Modifier.padding(horizontal = 20.dp))
            FilterCheckbox(
                label = "Male",
                isSelected = state.genderFilter == "Male",
                onCheckedChange = {
                    characterViewModel.updateGenderFilter(if (it) "Male" else "")
                }
            )
            FilterCheckbox(
                label = "Female",
                isSelected = state.genderFilter == "Female",
                onCheckedChange = {
                    characterViewModel.updateGenderFilter(if (it) "Female" else "")
                }
            )
            FilterCheckbox(
                label = "Genderless",
                isSelected = state.genderFilter == "Genderless",
                onCheckedChange = {
                    characterViewModel.updateGenderFilter(if (it) "Genderless" else "")
                }
            )
            FilterCheckbox(
                label = "Unknown",
                isSelected = state.genderFilter == "unknown",
                onCheckedChange = {
                    characterViewModel.updateGenderFilter(if (it) "unknown" else "")
                }
            )
        }
    }
}