package com.example.rickandmorty.presentation.common

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

@Composable
fun SearchFilter(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    suggestions: List<String>,
    onSuggestionSelected: (String) -> Unit,
    clearSearch: () -> Unit,
    placeholderText: String
) {
    val showSuggestions = searchQuery.isNotEmpty() && suggestions.isNotEmpty()
    Column {
        SearchBar(
            searchQuery = searchQuery,
            onValueChange = onValueChange,
            placeholderText = placeholderText,
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