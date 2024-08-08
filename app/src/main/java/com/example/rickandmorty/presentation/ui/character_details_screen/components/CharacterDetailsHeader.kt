package com.example.rickandmorty.presentation.ui.character_details_screen.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.rickandmorty.data.network.model.character.Character

@Composable
fun CharacterDetailsHeader(character : Character) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF03A9F4),
                        Color(0xFFFFFF00)
                    ),
                )
            ),
        contentAlignment = Alignment.Center,
    ) {
        AsyncImage(
            model = character.image,
            contentDescription = character.name,
            modifier = Modifier
                .size(150.dp)
                .clip(CircleShape),
        )
    }
    Text(
        text = character.name,
        modifier = Modifier.padding(16.dp),
        style = MaterialTheme.typography.titleLarge,
    )
    Text(
        text = character.status,
        style = MaterialTheme.typography.displayMedium,
    )
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceAround,

        ) {
        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = character.species,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Species",
                style = MaterialTheme.typography.displaySmall,
            )
        }

        Column (horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = character.gender,
                style = MaterialTheme.typography.titleLarge,
            )
            Text(
                text = "Gender",
                style = MaterialTheme.typography.displaySmall,
            )
        }
    }
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)) {
        Text(
            text = character.location.name,
            style = MaterialTheme.typography.titleMedium,
        )
        Text(
            text = "Location",
            style = MaterialTheme.typography.displaySmall,
        )
    }
}