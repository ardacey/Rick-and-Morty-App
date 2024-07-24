package com.example.rickandmorty.util

object Util {
    // https://rickandmortyapi.com/api
    const val BASE_URL = "https://rickandmortyapi.com/api/"
}

data class Resource<out T>(val data: T?, val error: Exception?) {
    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(data, null)
        }

        fun <T> error(error: Exception): Resource<T> {
            return Resource(null, error)
        }

        fun <T> loading(data: T?): Resource<T> {
            return Resource(null, null)
        }
    }
}