package com.example.rickandmorty.components.main_screen_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.rickandmorty.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBar(
    navController: NavController,
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
                    contentDescription = null
                )
                IconButton(
                    modifier = Modifier.padding(end = 8.dp),
                    onClick = { navController.navigate("settings") }
                ) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings")
                }
            }
        },
        modifier = Modifier.height(70.dp)
    )
}