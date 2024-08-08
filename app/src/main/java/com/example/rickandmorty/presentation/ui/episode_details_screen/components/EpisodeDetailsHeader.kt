package com.example.rickandmorty.presentation.ui.episode_details_screen.components

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
import com.example.rickandmorty.data.network.model.episode.Episode

@Composable
fun EpisodeDetailsHeader(episode : Episode) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Text(
            text = episode.episode,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.headlineLarge,
        )
        Text(
            text = episode.name,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.titleLarge,
        )
        Text(
            text = episode.airDate,
            modifier = Modifier.padding(16.dp),
            style = MaterialTheme.typography.displayMedium,
        )
    }
}