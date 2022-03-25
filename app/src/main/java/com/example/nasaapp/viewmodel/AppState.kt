package com.example.nasaapp.viewmodel

import com.example.nasaapp.model.AbstractDTO

sealed class AppState {
    data class Success(val podDTO: AbstractDTO) : AppState()
    data class Loading(val progress: Int?) : AppState()
    data class Error(val throwable: Throwable) : AppState()
}