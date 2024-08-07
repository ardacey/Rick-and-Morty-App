package com.example.rickandmorty.components.navigation_ui

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
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.rickandmorty.components.main_screen_ui.TopBar
import com.example.rickandmorty.navigation.Settings
import com.example.rickandmorty.navigation.character.CharacterDetailsScreen
import com.example.rickandmorty.navigation.character.CharactersScreen
import com.example.rickandmorty.navigation.episode.EpisodeDetailsScreen
import com.example.rickandmorty.navigation.episode.EpisodesScreen
import com.example.rickandmorty.navigation.location.LocationDetailsScreen
import com.example.rickandmorty.navigation.location.LocationsScreen

sealed class Screen(val route: String) {
    data object Characters : Screen("characters")
    data object Locations : Screen("locations")
    data object Episodes : Screen("episodes")
    data object Settings : Screen("settings")
    data object CharacterDetails : Screen("character_details/{characterId}") {
        fun createRoute(characterId: Int) = "character_details/$characterId"
    }
    data object LocationDetails : Screen("location_details/{locationId}") {
        fun createRoute(locationId: Int) = "location_details/$locationId"
    }
    data object EpisodeDetails : Screen("episode_details/{episodeId}") {
        fun createRoute(episodeId: Int) = "episode_details/$episodeId"
    }
}

@Composable
fun Navigation(
    isDarkMode: Boolean,
    onDarkModeToggle: (Boolean) -> Unit,
){
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
                    TopBar(navController = navController)
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
                    TopBar(navController = navController)
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
                    TopBar(navController = navController)
                    EpisodesScreen(navController)
                }
            }
            composable(Screen.Settings.route) {
                Settings(navController, isDarkMode, onDarkModeToggle, "1.0")
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