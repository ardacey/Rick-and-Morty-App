package com.example.rickandmorty.util

import com.example.rickandmorty.service.LocationAPI
import com.example.rickandmorty.util.Util.BASE_URL
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitLocationInstance {
    val api: LocationAPI by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(LocationAPI::class.java)
    }
}