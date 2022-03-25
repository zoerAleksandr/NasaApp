package com.example.nasaapp.model

import android.view.View

import android.widget.Toast


fun View.toast(
    message: String
) {
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}

fun View.show() {
    visibility = View.VISIBLE
}

fun View.hide() {
    visibility = View.GONE
}