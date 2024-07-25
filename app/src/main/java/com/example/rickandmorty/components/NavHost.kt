package com.example.rickandmorty.components

import android.annotation.SuppressLint
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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

@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun Navigation(isDarkMode: Boolean, onDarkModeToggle: (Boolean) -> Unit){
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryAsState().value?.destination?.route
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
            modifier = Modifier.padding(bottom = innerPadding.calculateBottomPadding())
        ) {
            composable(
                Screen.Characters.route,
                enterTransition = {
                    if (initialState.destination.route == Screen.Locations.route ||
                        initialState.destination.route == Screen.Episodes.route
                    ) {
                        slideInToRight(this)
                    } else {
                        null
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == Screen.Locations.route ||
                        targetState.destination.route == Screen.Episodes.route
                    ) {
                        slideOutToLeft(this)
                    } else {
                        null
                    }
                }
            ) {
                Column {
                    if (currentRoute == Screen.Characters.route) {
                        TopBar(isDarkMode = isDarkMode, onDarkModeToggle = onDarkModeToggle)
                    }
                    CharactersScreen(navController)
                }
            }
            composable(
                Screen.Locations.route,
                enterTransition = {
                    if (initialState.destination.route == Screen.Characters.route ||
                        initialState.destination.route == Screen.Episodes.route) {
                        if (initialState.destination.route == Screen.Characters.route) {
                            slideInToLeft(this)
                        } else {
                            slideInToRight(this)
                        }
                    } else {
                        null
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == Screen.Characters.route ||
                        targetState.destination.route == Screen.Episodes.route) {
                        if (targetState.destination.route == Screen.Characters.route) {
                            slideOutToRight(this)
                        } else {
                            slideOutToLeft(this)
                        }
                    } else {
                        null
                    }
                }
            ) {
                Column {
                    if (currentRoute == Screen.Locations.route) {
                        TopBar(isDarkMode = isDarkMode, onDarkModeToggle = onDarkModeToggle)
                    }
                    LocationsScreen(navController)
                }
            }
            composable(
                Screen.Episodes.route,
                enterTransition = {
                    if (initialState.destination.route == Screen.Characters.route ||
                        initialState.destination.route == Screen.Locations.route
                    ) {
                        slideInToLeft(this)
                    } else {
                        null
                    }
                },
                exitTransition = {
                    if (targetState.destination.route == Screen.Characters.route ||
                        targetState.destination.route == Screen.Locations.route
                    ) {
                        slideOutToRight(this)
                    } else {
                        null
                    }
                }
                ) {
                Column {
                    if (currentRoute == Screen.Episodes.route) {
                        TopBar(isDarkMode = isDarkMode, onDarkModeToggle = onDarkModeToggle)
                    }
                    EpisodesScreen(navController)
                }
            }
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

fun slideInToLeft(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return scope.slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

fun slideInToRight(scope: AnimatedContentTransitionScope<NavBackStackEntry>): EnterTransition {
    return scope.slideIntoContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}

fun slideOutToLeft(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return scope.slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(300)
    )
}

fun slideOutToRight(scope: AnimatedContentTransitionScope<NavBackStackEntry>): ExitTransition {
    return scope.slideOutOfContainer(
        AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(300)
    )
}