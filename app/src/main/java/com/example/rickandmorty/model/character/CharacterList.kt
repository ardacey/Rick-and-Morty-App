package com.example.rickandmorty.model.character

import com.example.rickandmorty.model.Metadata

data class CharacterList(
    val info: Metadata,
    val results: List<Character>
)
