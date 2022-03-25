package com.example.nasaapp.model

import com.google.gson.annotations.SerializedName

data class MarsPhotoListDTO(
    val photos: List<PodMarsPhotoDTO>?
) : AbstractDTO()

data class PodMarsPhotoDTO(
    val id: String?,
    @SerializedName("img_src")
    val image: String?,
    @SerializedName("earth_date")
    val earthDate: String?
)
