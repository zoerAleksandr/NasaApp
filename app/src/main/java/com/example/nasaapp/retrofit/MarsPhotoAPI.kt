package com.example.nasaapp.retrofit

import com.example.nasaapp.MarsPhotoListDTO
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface MarsPhotoAPI {
    @GET("mars-photos/api/v1/rovers/curiosity/photos")
    fun getMarsPhoto(
        @Query("earth_date") earth_date: String,
        @Query("api_key") api_key: String
    ): Call<MarsPhotoListDTO>
}