package com.example.rickandmorty.di

import com.example.rickandmorty.data.PreferencesManager
import com.example.rickandmorty.repository.CharacterDownload
import com.example.rickandmorty.repository.CharacterDownloadImpl
import com.example.rickandmorty.repository.EpisodeDownload
import com.example.rickandmorty.repository.EpisodeDownloadImpl
import com.example.rickandmorty.repository.LocationDownload
import com.example.rickandmorty.repository.LocationDownloadImpl
import com.example.rickandmorty.util.api.RetrofitCharacterInstance
import com.example.rickandmorty.util.api.RetrofitEpisodeInstance
import com.example.rickandmorty.util.api.RetrofitLocationInstance
import com.example.rickandmorty.viewmodel.character.CharacterDetailsViewModel
import com.example.rickandmorty.viewmodel.character.CharacterEpisodeViewModel
import com.example.rickandmorty.viewmodel.character.CharacterViewModel
import com.example.rickandmorty.viewmodel.episode.EpisodeCharacterViewModel
import com.example.rickandmorty.viewmodel.episode.EpisodeDetailsViewModel
import com.example.rickandmorty.viewmodel.episode.EpisodeViewModel
import com.example.rickandmorty.viewmodel.location.LocationCharacterViewModel
import com.example.rickandmorty.viewmodel.location.LocationDetailsViewModel
import com.example.rickandmorty.viewmodel.location.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
    single { PreferencesManager(get()) }
    single { RetrofitCharacterInstance.api }
    single { RetrofitEpisodeInstance.api }
    single { RetrofitLocationInstance.api }
    single<CharacterDownload> { CharacterDownloadImpl(get()) }
    single<EpisodeDownload> { EpisodeDownloadImpl(get()) }
    single<LocationDownload> { LocationDownloadImpl(get()) }
    viewModel { CharacterViewModel(get()) }
    viewModel { EpisodeViewModel(get()) }
    viewModel { LocationViewModel(get()) }
    viewModel { CharacterDetailsViewModel(get()) }
    viewModel { EpisodeDetailsViewModel(get()) }
    viewModel { LocationDetailsViewModel(get()) }
    viewModel { CharacterEpisodeViewModel(get()) }
    viewModel { EpisodeCharacterViewModel(get()) }
    viewModel { LocationCharacterViewModel(get()) }
}