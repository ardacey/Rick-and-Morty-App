package com.example.rickandmorty.model

data class Location(
    val id: Int?,
    val name: String?,
    val type: String?,
    val dimension: String?,
    val residents: ArrayList<String>?,
    val url: String?,
    val created: String?
)
