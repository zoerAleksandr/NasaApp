package com.example.nasaapp.model

import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.viewbinding.ViewBinding
import com.facebook.shimmer.ShimmerFrameLayout
import java.text.SimpleDateFormat
import java.util.*

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

// функции для изменения UI взависимости от состояния приложения
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