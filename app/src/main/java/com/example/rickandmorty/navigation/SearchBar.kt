package com.example.rickandmorty.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    searchQuery: String,
    onValueChange: (String) -> Unit,
    placeholderText: String
) {
    TextField(
        value = searchQuery,
        onValueChange = onValueChange,
        placeholder = { Text(placeholderText) },
        modifier = Modifier
            .fillMaxWidth()
            .padding(10.dp),
        textStyle = TextStyle(color = Color.White)
    )
}
