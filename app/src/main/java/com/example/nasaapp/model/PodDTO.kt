package com.example.nasaapp.model

import com.google.gson.annotations.SerializedName

open class AbstractDTO

data class PodDTO(
    val id: Long,
    val date: String?,
    val explanation: String?,
    @SerializedName("media_type")
    val mediaType: String?,
    @SerializedName("service_version")
    val serviceVersion: String?,
    val title: String?,
    val url: String?
) : AbstractDTO()
