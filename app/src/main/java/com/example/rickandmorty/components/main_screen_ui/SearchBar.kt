package com.example.rickandmorty.components.main_screen_ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    showOptionsSheet: (() -> Unit)? = null,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
    ) {
        TextField(
            value = searchQuery,
            onValueChange = onValueChange,
            placeholder = { Text(
                text = placeholderText,
                style = MaterialTheme.typography.displayMedium) },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = null) },
            trailingIcon = {
                if (showOptionsSheet != null) {
                    IconButton(onClick = { showOptionsSheet() }) {
                        Icon(imageVector = Icons.Filled.Settings, contentDescription = null)
                    }
                }
            }
        )
    }
}
