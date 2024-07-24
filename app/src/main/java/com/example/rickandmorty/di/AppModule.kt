package com.example.rickandmorty.di

import com.example.rickandmorty.repository.CharacterDownload
import com.example.rickandmorty.repository.CharacterDownloadImpl
import com.example.rickandmorty.repository.EpisodeDownload
import com.example.rickandmorty.repository.EpisodeDownloadImpl
import com.example.rickandmorty.repository.LocationDownload
import com.example.rickandmorty.repository.LocationDownloadImpl
import com.example.rickandmorty.util.RetrofitCharacterInstance
import com.example.rickandmorty.util.RetrofitEpisodeInstance
import com.example.rickandmorty.util.RetrofitLocationInstance
import com.example.rickandmorty.viewmodel.CharacterDetailsViewModel
import com.example.rickandmorty.viewmodel.CharacterEpisodeViewModel
import com.example.rickandmorty.viewmodel.CharacterViewModel
import com.example.rickandmorty.viewmodel.EpisodeCharacterViewModel
import com.example.rickandmorty.viewmodel.EpisodeDetailsViewModel
import com.example.rickandmorty.viewmodel.EpisodeViewModel
import com.example.rickandmorty.viewmodel.LocationCharacterViewModel
import com.example.rickandmorty.viewmodel.LocationDetailsViewModel
import com.example.rickandmorty.viewmodel.LocationViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module {
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