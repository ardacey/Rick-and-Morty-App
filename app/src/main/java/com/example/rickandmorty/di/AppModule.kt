package com.example.rickandmorty.di

import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.repository.CharacterRepository
import com.example.rickandmorty.repository.CharacterRepositoryImpl
import com.example.rickandmorty.repository.EpisodeRepository
import com.example.rickandmorty.repository.EpisodeRepositoryImpl
import com.example.rickandmorty.repository.LocationRepository
import com.example.rickandmorty.repository.LocationRepositoryImpl
import com.example.rickandmorty.util.api.RetrofitCharacterInstance
import com.example.rickandmorty.util.api.RetrofitEpisodeInstance
import com.example.rickandmorty.util.api.RetrofitLocationInstance
import com.example.rickandmorty.viewmodel.character.CharacterDetailsViewModel
import com.example.rickandmorty.viewmodel.character.CharacterViewModel
import com.example.rickandmorty.viewmodel.episode.EpisodeDetailsViewModel
import com.example.rickandmorty.viewmodel.episode.EpisodeViewModel
import com.example.rickandmorty.viewmodel.location.LocationDetailsViewModel
import com.example.rickandmorty.viewmodel.location.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PreferencesManager(get()) }
    single { RetrofitCharacterInstance.api }
    single { RetrofitEpisodeInstance.api }
    single { RetrofitLocationInstance.api }
    single<CharacterRepository> { CharacterRepositoryImpl(get()) }
    single<EpisodeRepository> { EpisodeRepositoryImpl(get()) }
    single<LocationRepository> { LocationRepositoryImpl(get()) }
    viewModel { CharacterViewModel(get()) }
    viewModel { EpisodeViewModel(get()) }
    viewModel { LocationViewModel(get()) }
    viewModel {p -> CharacterDetailsViewModel(p[0], get(), get()) }
    viewModel {p -> EpisodeDetailsViewModel(p[0], get(), get()) }
    viewModel {p -> LocationDetailsViewModel(p[0], get(), get()) }
}