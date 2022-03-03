package com.example.nasaapp.model.room

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PodEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val title: String,
    val date: String,
    val description: String,
    val imageURL: String
)