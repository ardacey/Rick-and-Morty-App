package com.example.rickandmorty.data.network.model.location

import com.example.rickandmorty.data.network.model.Metadata

data class LocationList(
    val info: Metadata,
    val results: List<Location>
)
