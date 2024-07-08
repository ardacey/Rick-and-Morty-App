package com.example.rickandmorty.model

data class Character(
    val id: Int?,
    val name: String?,
    val status: String?,
    val species: String?,
    val type: String?,
    val gender: String?,
    val origin: CharacterLocation?,
    val location: CharacterLocation?,
    val image: String?,
    val episode: ArrayList<String>?,
    val url: String?,
    val created: String?
)
