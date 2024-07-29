package com.example.rickandmorty

import android.app.Application
import com.example.rickandmorty.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class RickAndMortyApp : Application() {
    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@RickAndMortyApp)
            modules(appModule)
        }
    }
}