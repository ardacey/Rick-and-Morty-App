package com.example.rickandmorty.presentation.common

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    showOptionsSheet: (() -> Unit)? = null,
    clearSearch: (() -> Unit)? = null
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isFocused by interactionSource.collectIsFocusedAsState()

    OutlinedTextField(
        value = searchQuery,
        onValueChange = onValueChange,
        placeholder = { Text(
            text = placeholderText,
            style = MaterialTheme.typography.displayMedium) },
        shape = RoundedCornerShape(15),
        modifier = Modifier.fillMaxWidth().padding(10.dp),
        maxLines = 1,
        leadingIcon = { Icon(imageVector = Icons.Filled.Search, contentDescription = "Search") },
        trailingIcon = {
            if (isFocused) {
                IconButton(onClick = { clearSearch?.invoke() }) {
                    Icon(imageVector = Icons.Filled.Close, contentDescription = "Clear")
                }
            } else {
                if (showOptionsSheet != null) {
                    IconButton(onClick = { showOptionsSheet() }) {
                        Icon(imageVector = Icons.Default.MoreVert, contentDescription = "Options")
                    }
                }
            }
        },
        interactionSource = interactionSource
    )
}
