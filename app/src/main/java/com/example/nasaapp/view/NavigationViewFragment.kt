package com.example.nasaapp.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.nasaapp.R
import com.example.nasaapp.databinding.NavigationLayoutBinding
import com.example.nasaapp.model.hide
import com.example.nasaapp.model.toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class NavigationViewFragment : BottomSheetDialogFragment() {

    private val binding: NavigationLayoutBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.navigation_layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener { menu ->
            when (menu.itemId) {
                R.id.menu_navigation_view_favorite -> {
                    binding.root.toast("Открываю список избранных картинок")
                }
                R.id.menu_navigation_view_setting -> {
                    activity?.let {
                        it.supportFragmentManager.beginTransaction()
                            .replace(R.id.container, SettingsFragment.newInstance())
                            .commit()
                        binding.root.hide()
                        onDestroyView()
                    }
                }
            }
            true
        }
    }
}