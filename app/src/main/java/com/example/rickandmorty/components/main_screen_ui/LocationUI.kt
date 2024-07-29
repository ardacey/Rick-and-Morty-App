package com.example.rickandmorty.components.main_screen_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.model.Location

@Composable
fun LocationUI(
    location: Location,
    isFavorite: Boolean,
    onClick: () -> Unit = { },
    onFavoriteClick: () -> Unit = {}
) {
    Card(
        Modifier
            .size(150.dp)
            .padding(10.dp)
            .clickable { onClick.invoke() },
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 6.dp)
    ) {
        Column(
            modifier = Modifier
                .wrapContentSize()
                .padding(10.dp)
        ) {
            Row(
                modifier = Modifier.padding(vertical = 6.dp).fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = location.name,
                    style = MaterialTheme.typography.titleMedium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.weight(1f)
                )
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite",
                    modifier = Modifier.clickable { onFavoriteClick.invoke() }
                )
            }

            Text(
                text = location.type,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(vertical = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = location.dimension,
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.padding(vertical = 4.dp),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        }
    }
}