package com.example.rickandmorty.model.episode

import com.example.rickandmorty.model.Metadata

data class EpisodeList(
    val info: Metadata,
    val results: List<Episode>
)
