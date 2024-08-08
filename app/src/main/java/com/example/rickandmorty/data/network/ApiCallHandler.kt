package com.example.rickandmorty.data.network

import com.example.rickandmorty.data.network.model.basemodel.APIErrorResponse
import com.example.rickandmorty.data.network.model.basemodel.AppResult
import com.google.gson.GsonBuilder
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import retrofit2.Response

object ApiCallHandler {
    suspend fun <T> execute(
        request: suspend () -> Response<T>
    ): AppResult<T> {
        var response: Response<T>? = null
        val job = CoroutineScope(Dispatchers.IO).async {
            try {
                response = request.invoke()
            } catch (e: Exception) {
                return@async AppResult.Error(message = e.message ?: "Error")
            }
        }
        job.await()
        when (response?.code()) {
            200 -> {
                response?.body()?.let { return AppResult.Success(it) } ?: return AppResult.Error()
            }

            else -> {
                parseError(response)
                return AppResult.Error()
            }
        }
    }

    private fun parseError(response: Response<*>?): AppResult.Error {
        val gson = GsonBuilder().create()
        val error: APIErrorResponse
        try {
            error = gson.fromJson(response?.errorBody()?.string(), APIErrorResponse::class.java)
        } catch (e: Exception) {
            return AppResult.Error(message = e.message ?: "Error")
        }
        return error.error
    }
}