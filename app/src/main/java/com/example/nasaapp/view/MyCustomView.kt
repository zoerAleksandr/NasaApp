package com.example.nasaapp.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView

class MyCustomView
@JvmOverloads
constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        val radius = height / 4
        val centerX = width / 2
        val centerY = height / 2
        val paint = Paint().apply {
            isAntiAlias
            color = Color.LTGRAY
            alpha
            strokeWidth = 2f
            style = Paint.Style.FILL
        }
        val paintText = Paint().apply {
            textSize = 38f
        }
        val rectF = RectF().apply {
            set(
                (centerX - radius).toFloat(),
                (centerY - radius).toFloat(),
                (centerX + radius).toFloat(),
                (centerY + radius).toFloat()
            )
        }
        canvas?.let {
            it.drawRoundRect(rectF, 100f, 100f, paint)
            it.drawText(
                "My first Custom View",
                centerX.toFloat() - 180,
                centerY.toFloat(),
                paintText
            )
        }
    }

}