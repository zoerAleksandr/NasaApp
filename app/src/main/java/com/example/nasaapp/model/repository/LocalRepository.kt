package com.example.nasaapp.model.repository

import com.example.nasaapp.model.PodDTO

interface LocalRepository {
    fun getAllPod(): List<PodDTO>
    fun addPod(podDTO: PodDTO)
    fun removePod(podDTO: PodDTO)
}