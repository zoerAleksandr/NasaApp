package com.example.nasaapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.nasaapp.ui.main.MainFragment

const val THEME_ID = "THEME_ID"

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changedTheme()
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance())
                .commitNow()
        }
    }

    private fun changedTheme() {
        setTheme(this.getPreferences(MODE_PRIVATE).getInt(THEME_ID, R.style.Theme_NasaApp))
    }
}