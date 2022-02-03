package com.example.nasaapp

sealed class AppState {
    data class Success(val podDTO: PodDTO) : AppState()
    data class Loading(val progress: Int?) : AppState()
    data class Error(val throwable: Throwable) : AppState()
}
