package com.example.rickandmorty.components.details_screen_ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.model.location.Location

@Composable
fun LocationDetailsHeader(location : Location) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Text(
            text = location.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = location.type,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.displayMedium,
        )
        Text(
            text = location.dimension,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.displayMedium,
        )
    }
}