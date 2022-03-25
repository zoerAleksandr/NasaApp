package com.example.nasaapp.viewmodel

sealed class SelectedTab {
    object Earth : SelectedTab()
    object Mars : SelectedTab()
}