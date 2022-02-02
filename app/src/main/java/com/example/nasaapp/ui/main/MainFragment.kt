package com.example.nasaapp.ui.main

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.Layout
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.nasaapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setBottomSheetBehavior(view.findViewById(R.id.bottom_sheet_container))
    }

    private fun setBottomSheetBehavior(layout: FrameLayout){
        bottomSheetBehavior = BottomSheetBehavior.from(layout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }
}