package com.example.nasaapp.view

import com.example.nasaapp.model.PodDTO

interface RemoveItemCallback {
    fun remove(podDTO: PodDTO)
}