package com.example.rickandmorty.model.location

import com.example.rickandmorty.model.Metadata

data class LocationList(
    val info: Metadata,
    val results: List<Location>
)
