package com.example.rickandmorty.data.model.episode

import com.example.rickandmorty.data.model.Metadata

data class EpisodeList(
    val info: Metadata,
    val results: List<Episode>
)
