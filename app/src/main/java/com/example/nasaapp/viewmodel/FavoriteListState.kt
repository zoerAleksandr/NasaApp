package com.example.nasaapp.viewmodel

import com.example.nasaapp.model.PodDTO

sealed class FavoriteListState{
    data class Success(val listDTO: List<PodDTO>) : FavoriteListState()
    data class Loading(val progress: Int?) : FavoriteListState()
    data class Error(val throwable: Throwable) : FavoriteListState()
}
