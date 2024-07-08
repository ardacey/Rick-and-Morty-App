package com.example.rickandmorty.model

data class EpisodeList(
    val info: Metadata,
    val results: List<Episode>
)
