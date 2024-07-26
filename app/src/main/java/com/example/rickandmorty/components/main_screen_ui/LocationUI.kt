package com.example.rickandmorty.components.main_screen_ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.model.Location

@Composable
fun LocationUI(location: Location, onClick: () -> Unit = { }) {
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
            Text(
                text = location.name,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(vertical = 4.dp),
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
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