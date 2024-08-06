package com.example.rickandmorty.data.model.character

import com.example.rickandmorty.data.model.Metadata

data class CharacterList(
    val info: Metadata,
    val results: List<Character>
)
