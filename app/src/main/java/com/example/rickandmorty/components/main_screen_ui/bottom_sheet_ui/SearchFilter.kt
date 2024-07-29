package com.example.rickandmorty.components.main_screen_ui.bottom_sheet_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.components.main_screen_ui.SearchBar

@Composable
fun SearchFilter(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
    onSuggestionSelected: (String) -> Unit,
    clearSearch: () -> Unit
) {
    val showSuggestions = searchQuery.isNotEmpty() && suggestions.isNotEmpty()
    Column {
        SearchBar(
            searchQuery = searchQuery,
            onValueChange = onValueChange,
            placeholderText = "",
            clearSearch = clearSearch
        )
    }
    if (showSuggestions) {
        LazyColumn {
            items(suggestions) { suggestion ->
                Text(
                    text = suggestion,
                    style = MaterialTheme.typography.displayMedium,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                        .clickable {
                            onSuggestionSelected(suggestion)
                            onValueChange(suggestion)
                        }
                )
            }
        }
    }
}