package com.example.rickandmorty.data.network.model.episode

import com.example.rickandmorty.data.network.model.Metadata

data class EpisodeList(
    val info: Metadata,
    val results: List<Episode>
)
