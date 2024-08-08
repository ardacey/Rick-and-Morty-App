package com.example.rickandmorty.presentation.ui.character_details_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.data.network.model.episode.Episode

@Composable
fun EpisodeCard(episode: Episode, onClick: () -> Unit = { }) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Face,
                contentDescription = null,
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(48.dp)
            )
            Column {
                Text(
                    text = episode.episode,
                    style = MaterialTheme.typography.titleMedium,
                )
                Text(
                    text = episode.name,
                    style = MaterialTheme.typography.displayMedium,
                    maxLines = 1,
                    modifier = Modifier.width(200.dp),
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
        Button(
            onClick = { onClick.invoke() },
        ) {
            Icon(
                imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                contentDescription = "Go to the Episode",
                modifier = Modifier
                    .padding(horizontal = 8.dp)
                    .size(30.dp)
            )
        }
    }
}