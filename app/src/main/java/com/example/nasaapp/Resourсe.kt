package com.example.nasaapp

sealed class Resource {
    object Earth : Resource()
    object Mars : Resource()
}