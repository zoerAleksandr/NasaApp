package com.example.nasaapp.retrofit

import com.example.nasaapp.PodDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
import java.util.*

interface PodTodayAPI {
    @GET("planetary/apod")
    fun getPod(
        @Query("api_key") apiKey: String
    ): Call<PodDTO>
}

interface PodYesterdayAPI {
    @GET("planetary/apod")
    fun getPod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Call<PodDTO>
}

interface PodBeforeDayAPI {
    @GET("planetary/apod")
    fun getPod(
        @Query("api_key") apiKey: String,
        @Query("date") date: String
    ): Call<PodDTO>
}