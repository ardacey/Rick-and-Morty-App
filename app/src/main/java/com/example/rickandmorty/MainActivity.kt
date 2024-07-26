package com.example.rickandmorty

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
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
import com.example.rickandmorty.di.appModule
import com.example.rickandmorty.components.Navigation
import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.ui.theme.RickAndMortyTheme
import kotlinx.coroutines.launch
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        startKoin {
            androidContext(this@MainActivity)
            modules(appModule)
        }

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