package com.example.rickandmorty.components.main_screen_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.rickandmorty.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    isDarkMode : Boolean,
    onClick : () -> Unit,
    onDarkModeToggle : (Boolean) -> Unit
) {
    TopAppBar(
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo),
                    contentDescription = null)
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End,
                    modifier = Modifier.padding(end = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = "Favorites",
                        modifier = Modifier.clickable { onClick() }
                    )
                    Switch(
                        checked = isDarkMode,
                        onCheckedChange = { onDarkModeToggle(it) },
                        modifier = Modifier.padding(horizontal = 10.dp)
                    )
                }

            }
        },
        modifier = Modifier.height(70.dp)
    )
}