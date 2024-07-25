package com.example.rickandmorty.components

import android.os.Build
import androidx.annotation.RequiresApi
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

sealed class Screen(val route: String) {
    object Characters : Screen("characters")
    object Locations : Screen("locations")
    object Episodes : Screen("episodes")
    object CharacterDetails : Screen("character_details/{characterId}") {
        fun createRoute(characterId: Int) = "character_details/$characterId"
    }
    object LocationDetails : Screen("location_details/{locationId}") {
        fun createRoute(locationId: Int) = "location_details/$locationId"
    }
    object EpisodeDetails : Screen("episode_details/{episodeId}") {
        fun createRoute(episodeId: Int) = "episode_details/$episodeId"
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(){
    val navController = rememberNavController()
    val items = listOf(
        BottomNavItem("Characters", Icons.Filled.Face, Screen.Characters.route),
        BottomNavItem("Locations", Icons.Filled.LocationOn, Screen.Locations.route),
        BottomNavItem("Episodes", Icons.Filled.Menu, Screen.Episodes.route)
    )
    Scaffold(
        bottomBar = { BottomNavigationBar(navController = navController, items = items) }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Characters.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Characters.route) { CharactersScreen(navController) }
            composable(Screen.Locations.route) { LocationsScreen(navController) }
            composable(Screen.Episodes.route) { EpisodesScreen(navController) }
            composable(
                Screen.CharacterDetails.route,
                arguments = listOf(navArgument("characterId") { type = NavType.IntType })
            ) { backStackEntry ->
                val characterId = backStackEntry.arguments?.getInt("characterId")
                CharacterDetailsScreen(characterId, navController)
            }
            composable(
                Screen.LocationDetails.route,
                arguments = listOf(navArgument("locationId") { type = NavType.IntType })
            ) { backStackEntry ->
                val locationId = backStackEntry.arguments?.getInt("locationId")
                LocationDetailsScreen(locationId, navController)
            }
            composable(
                Screen.EpisodeDetails.route,
                arguments = listOf(navArgument("episodeId") { type = NavType.IntType })
            ) { backStackEntry ->
                val episodeId = backStackEntry.arguments?.getInt("episodeId")
                EpisodeDetailsScreen(episodeId, navController)
            }
        }
    }
}