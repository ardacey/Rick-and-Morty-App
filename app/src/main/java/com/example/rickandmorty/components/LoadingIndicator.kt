package com.example.rickandmorty.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.zIndex

@Composable
fun LoadingIndicator() {
    Box(
        modifier = Modifier.fillMaxSize().zIndex(10f),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator()
    }
}