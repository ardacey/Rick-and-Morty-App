package com.example.rickandmorty.model

data class Episode(
    val id: Int?,
    val name: String?,
    val airDate: String?,
    val episode: String?,
    val characters: ArrayList<String>?,
    val url: String?,
    val created: String?
)
