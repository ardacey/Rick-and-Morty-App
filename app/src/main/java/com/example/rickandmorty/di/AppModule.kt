package com.example.rickandmorty.di

import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.data.repository.CharacterRepository
import com.example.rickandmorty.data.repository.CharacterRepositoryImpl
import com.example.rickandmorty.data.repository.EpisodeRepository
import com.example.rickandmorty.data.repository.EpisodeRepositoryImpl
import com.example.rickandmorty.data.repository.LocationRepository
import com.example.rickandmorty.data.repository.LocationRepositoryImpl
import com.example.rickandmorty.utils.api.RetrofitCharacterInstance
import com.example.rickandmorty.utils.api.RetrofitEpisodeInstance
import com.example.rickandmorty.utils.api.RetrofitLocationInstance
import com.example.rickandmorty.presentation.ui.character_details_screen.CharacterDetailsViewModel
import com.example.rickandmorty.presentation.ui.characters_screen.CharacterViewModel
import com.example.rickandmorty.presentation.ui.episode_details_screen.EpisodeDetailsViewModel
import com.example.rickandmorty.presentation.ui.episodes_screen.EpisodeViewModel
import com.example.rickandmorty.presentation.ui.location_details_screen.LocationDetailsViewModel
import com.example.rickandmorty.presentation.ui.locations_screen.LocationViewModel
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