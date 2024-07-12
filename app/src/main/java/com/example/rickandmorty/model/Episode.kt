package com.example.rickandmorty.model

data class Episode(
    val id: Int = 0,
    val name: String = "",
    val airDate: String = "",
    val episode: String = "",
    val characters: ArrayList<String> = arrayListOf(),
    val url: String = "",
    val created: String = "",
)
