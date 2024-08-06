package com.example.rickandmorty.data.model.location

data class Location(
    val id: Int  = 0,
    val name: String = "",
    val type: String = "",
    val dimension: String = "",
    val residents: ArrayList<String> = arrayListOf(),
    val url: String = "",
    val created: String = "",
)
