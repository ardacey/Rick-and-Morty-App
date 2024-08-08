package com.example.rickandmorty.data.network.model.character

import com.example.rickandmorty.data.network.model.Metadata

data class CharacterList(
    val info: Metadata,
    val results: List<Character>
)
