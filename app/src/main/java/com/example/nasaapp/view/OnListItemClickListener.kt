package com.example.nasaapp.view

import com.example.nasaapp.model.PodDTO

fun interface OnListItemClickListener {
    fun onItemClick(data: PodDTO)
}