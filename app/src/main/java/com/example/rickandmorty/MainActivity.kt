package com.example.rickandmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.example.rickandmorty.components.navigation_ui.Navigation
import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        installSplashScreen()
        enableEdgeToEdge()

        val preferencesManager = PreferencesManager(this)

        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            val scope = rememberCoroutineScope()

            LaunchedEffect(Unit) {
                preferencesManager.darkModeFlow.collect { darkMode ->
                    isDarkMode = darkMode
                }
            }

            RickAndMortyTheme(
                darkTheme = isDarkMode
            ) {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Navigation(
                        isDarkMode = isDarkMode,
                        onDarkModeToggle = {
                            isDarkMode = it
                            scope.launch {
                                preferencesManager.updateDarkMode(it)
                            }
                        }
                    )
                }
            }
        }
    }
}