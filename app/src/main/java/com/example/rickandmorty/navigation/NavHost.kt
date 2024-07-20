package com.example.rickandmorty.navigation

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
fun Navigation(){
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("Characters",Icons.Filled.Face, "characters"),
        BottomNavItem("Locations", Icons.Filled.LocationOn, "locations"),
        BottomNavItem("Episodes", Icons.Filled.Menu, "episodes")
    )
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, items = items) }
    ) {
        innerPadding ->
        NavHost(
            navController = navController,
            startDestination = "characters",
            modifier = Modifier.padding(innerPadding)
        ) {
            composable("characters") { CharactersScreen(navController) }
            composable("locations") { LocationsScreen(navController) }
            composable("episodes") { EpisodesScreen(navController) }

            composable(
                "Character Details/{characterId}",
                arguments = listOf(navArgument("characterId") { type = NavType.IntType })
            ) {
                id -> CharacterDetailsScreen(id.arguments?.getInt("characterId"), navController)
            }

            composable(
                "Location Details/{locationId}",
                arguments = listOf(navArgument("locationId") { type = NavType.IntType })
            ) { id ->
                LocationDetailsScreen(id.arguments?.getInt("locationId"), navController)
            }

            composable(
                "Episode Details/{episodeId}",
                arguments = listOf(navArgument("episodeId") { type = NavType.IntType })
            ) { id ->
                EpisodeDetailsScreen(id.arguments?.getInt("episodeId"), navController)
            }
        }
    }
}

@Composable
fun BottomNavigationBar(navController: NavController, items: List<BottomNavItem>) {
    BottomAppBar(
        modifier = Modifier.height(75.dp)
    ){
        Row(
            horizontalArrangement = Arrangement.SpaceAround,
            modifier = Modifier.fillMaxWidth()
        ) {
            items.forEach { item ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    IconButton(onClick = { navController.navigate(item.route) }) {
                        Icon(imageVector = item.icon, contentDescription = item.title)
                    }
                }
            }
        }
    }
}

data class BottomNavItem(val title: String, val icon: ImageVector, val route: String)