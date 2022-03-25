package com.example.nasaapp.view

import android.animation.Animator
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import android.view.animation.AccelerateInterpolator
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.R

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        val text = findViewById<TextView>(R.id.text)
        val logoImage = findViewById<ImageView>(R.id.logo)

        val animText = ObjectAnimator.ofFloat(text, "alpha", 0f, 1f).apply {
            duration = 500
            addListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    // Нечего делать
                }
                override fun onAnimationEnd(animation: Animator?) {
                    startActivity(Intent(this@SplashActivity, MainActivity::class.java))
                    finish()
                }
                override fun onAnimationCancel(animation: Animator?) {
                    // Нечего делать
                }
                override fun onAnimationRepeat(animation: Animator?) {
                    // Нечего делать
                }
            })
        }

        logoImage.animate()
            .setDuration(1500)
            .scaleY(1.1f)
            .scaleX(1.1f)
            .alpha(1f)
            .setInterpolator(AccelerateInterpolator())
            .setListener(object : Animator.AnimatorListener {
                override fun onAnimationStart(animation: Animator?) {
                    // Нечего делать
                }
                override fun onAnimationEnd(animation: Animator?) {
                    animText.start()
                }
                override fun onAnimationCancel(animation: Animator?) {
                    // Нечего делать
                }
                override fun onAnimationRepeat(animation: Animator?) {
                    // Нечего делать
                }
            })
    }
}