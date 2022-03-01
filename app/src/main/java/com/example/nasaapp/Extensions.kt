package com.example.nasaapp

import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import java.text.SimpleDateFormat
import java.util.*

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

// функции для получения даты в String
private val nasaFormat = SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH)

fun getToday(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, 0)
    return nasaFormat.format(calendar.time)
}

fun getYesterday(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -1)
    return nasaFormat.format(calendar.time)
}

fun getBeforeDay(): String {
    val calendar = Calendar.getInstance()
    calendar.add(Calendar.DATE, -2)
    return nasaFormat.format(calendar.time)
}

fun setViewStateLoading(
    container: ConstraintLayout,
    bottomSheet: FrameLayout,
    shimmerLayout: ShimmerFrameLayout,
    binding: ViewBinding
) {
    binding.apply {
        container.hide()
        bottomSheet.hide()
        shimmerLayout.show()
        shimmerLayout.startShimmer()
    }
}

fun setViewStateSuccess(
    container: ConstraintLayout,
    bottomSheet: FrameLayout,
    shimmerLayout: ShimmerFrameLayout,
    binding: ViewBinding
) {
    binding.apply {
        shimmerLayout.stopShimmer()
        shimmerLayout.hide()
        bottomSheet.show()
        container.show()
    }
}