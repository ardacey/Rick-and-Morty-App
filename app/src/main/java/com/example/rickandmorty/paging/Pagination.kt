package com.example.rickandmorty.paging

interface Pagination<Item, Value> {
    suspend fun loadNextItems()
    fun reset()
}