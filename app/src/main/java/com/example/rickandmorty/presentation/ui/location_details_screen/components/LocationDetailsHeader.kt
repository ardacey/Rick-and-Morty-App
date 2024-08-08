package com.example.rickandmorty.presentation.ui.location_details_screen.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rickandmorty.data.network.model.location.Location

@Composable
fun LocationDetailsHeader(
    location : Location,
    navController: NavController
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        Row(
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            IconButton(onClick = { navController.popBackStack() }) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Back",
                    modifier = Modifier.size(48.dp))
            }
        }
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