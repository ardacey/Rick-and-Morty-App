package com.example.rickandmorty.data.network.model

data class Metadata(
    val count: Int,
    val pages: Int,
    val next: String,
    val prev: String
)