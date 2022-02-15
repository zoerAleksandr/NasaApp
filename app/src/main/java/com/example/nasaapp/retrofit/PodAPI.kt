package com.example.nasaapp.retrofit

import com.example.nasaapp.PodDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface PodAPI {
    @GET("planetary/apod")
    fun getPod(
        @Query("api_key") api_key: String,
        @Query("date") date: String
    ): Call<PodDTO>
}