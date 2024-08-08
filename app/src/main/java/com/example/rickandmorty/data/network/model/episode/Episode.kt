package com.example.rickandmorty.data.network.model.episode

import com.google.gson.annotations.SerializedName

data class Episode(
    val id: Int = 0,
    val name: String = "",
    @SerializedName("air_date")
    val airDate: String = "",
    val episode: String = "",
    val characters: ArrayList<String> = arrayListOf(),
    val url: String = "",
    val created: String = "",
)
