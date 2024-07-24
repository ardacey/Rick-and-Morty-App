package com.example.rickandmorty.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState

@Composable
fun BottomNavigationBar(navController: NavController, items: List<BottomNavItem>) {
    val currentBackEntry = navController.currentBackStackEntryAsState()
    val currentRoute = currentBackEntry.value?.destination?.route

    BottomAppBar(
        modifier = Modifier.height(80.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                val selected = currentRoute == item.route
                IconButton(
                    onClick = { navController.navigate(item.route) },
                    modifier = Modifier.weight(1f)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.title,
                            modifier = Modifier
                                .size(20.dp)
                                .graphicsLayer {
                                    alpha = if (selected) 1f else 0.4f
                                })
                        Text(
                            text = item.title,
                            fontSize = 12.sp,
                            color = if (selected) Color.Black else Color.Gray)
                    }
                }
            }
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)