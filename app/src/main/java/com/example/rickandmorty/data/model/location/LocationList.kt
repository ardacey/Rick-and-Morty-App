package com.example.rickandmorty.data.model.location

import com.example.rickandmorty.data.model.Metadata

data class LocationList(
    val info: Metadata,
    val results: List<Location>
)
