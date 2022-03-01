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
        when (styleId) {
            R.style.Theme_Mars -> {
                binding.cardMoon.alpha = 0.3f
            }
            R.style.Theme_Moon -> {
                binding.cardMars.alpha = 0.3f
            }
        }

        binding.cardMars.setOnClickListener {
            activity?.let {
                with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                    putInt(THEME_ID, R.style.Theme_Mars)
                    apply()
                }
                binding.cardMars.animate()
                    .alpha(1f)
                binding.cardMoon.animate()
                    .alpha(0.3f)
                requireActivity().recreate()

            }
        }

        binding.cardMoon.setOnClickListener {
            activity?.let {
                with(it.getPreferences(Context.MODE_PRIVATE).edit()) {
                    putInt(THEME_ID, R.style.Theme_Moon)
                    apply()
                }
                binding.cardMoon.animate()
                    .alpha(1f)
                binding.cardMars.animate()
                    .alpha(0.3f)
                requireActivity().recreate()
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
