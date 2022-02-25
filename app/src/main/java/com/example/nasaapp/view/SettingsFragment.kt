package com.example.nasaapp.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.nasaapp.R
import com.example.nasaapp.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment(R.layout.fragment_settings) {

    companion object {
        fun newInstance() = SettingsFragment()
    }

    private val binding: FragmentSettingsBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val styleId = activity?.let {
            it.getPreferences(Context.MODE_PRIVATE)?.getInt(THEME_ID, R.style.Theme_NasaApp)
        }

        val radioId = when (styleId) {
            R.style.Theme_NasaApp -> R.id.radio_btn_default_theme
            R.style.Theme_Mars -> R.id.radio_btn_mars_theme
            R.style.Theme_Moon -> R.id.radio_btn_moon_theme
            else -> -1
        }
        binding.radioGroupChangedTheme.check(radioId)

        binding.radioGroupChangedTheme.setOnCheckedChangeListener { _, checkedId ->
            when (checkedId) {
                R.id.radio_btn_default_theme -> {
                    activity?.let {
                        with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                            putInt(THEME_ID, R.style.Theme_NasaApp)
                            apply()
                        }
                    }
                }
                R.id.radio_btn_mars_theme -> {
                    activity?.let {
                        with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                            putInt(THEME_ID, R.style.Theme_Mars)
                            apply()
                        }
                    }
                }
                R.id.radio_btn_moon_theme -> {
                    activity?.let {
                        with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                            putInt(THEME_ID, R.style.Theme_Moon)
                            apply()
                        }
                    }
                }
            }
        }

        binding.toolbar.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.btn_save -> {
                    startActivity(Intent(requireContext(), MainActivity::class.java))
                    true
                }
                else -> {
                    false
                }
            }
        }
    }

}
