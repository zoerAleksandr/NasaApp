package com.example.nasaapp.ui.main

import android.os.Bundle
import android.view.*
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.nasaapp.R
import com.example.nasaapp.databinding.NavigationLayoutBinding
import com.example.nasaapp.toast
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

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        binding.navigationView.setNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_navigation_view_favorite -> {
                    binding.root.toast("Открываю список избранных картинок")
                }
                R.id.menu_navigation_view_setting -> {
                    binding.root.toast("Открываю экран настроек")
                }
            }
            true
        }
    }
}