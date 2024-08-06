package com.example.rickandmorty.data.model.basemodel

sealed class AppResult<out T> {
    data class Success<out T>(val successData: T) : AppResult<T>()
    class Error(
        val message: String = "Error",
    ) : AppResult<Nothing>()
}

data class APIErrorResponse(val success: Boolean, val error: AppResult.Error)