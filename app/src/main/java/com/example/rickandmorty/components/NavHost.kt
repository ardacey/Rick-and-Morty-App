package com.example.rickandmorty.components

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickandmorty.navigation.CharacterDetailsScreen
import com.example.rickandmorty.navigation.CharactersScreen
import com.example.rickandmorty.navigation.EpisodeDetailsScreen
import com.example.rickandmorty.navigation.EpisodesScreen
import com.example.rickandmorty.navigation.LocationDetailsScreen
import com.example.rickandmorty.navigation.LocationsScreen

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