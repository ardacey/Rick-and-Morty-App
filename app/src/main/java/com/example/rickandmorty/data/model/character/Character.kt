package com.example.rickandmorty.data.model.character

data class Character(
    val id: Int = 0,
    val name: String = "",
    val status: String = "",
    val species: String = "",
    val type: String = "",
    val gender: String = "",
    val origin: CharacterLocation = CharacterLocation(),
    val location: CharacterLocation = CharacterLocation(),
    val image: String = "",
    val episode: ArrayList<String> = arrayListOf(),
    val url: String = "",
    val created: String = ""
)
