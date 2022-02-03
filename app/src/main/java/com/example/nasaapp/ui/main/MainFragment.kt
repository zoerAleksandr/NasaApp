package com.example.nasaapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.nasaapp.AppState
import com.example.nasaapp.R
import com.example.nasaapp.databinding.MainFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val binding: MainFragmentBinding by viewBinding()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setBottomSheetBehavior(binding.includeBottomSheet.bottomSheetContainer)
        viewModel.getData().observe(viewLifecycleOwner, { appState ->
            renderData(appState)
        }
        )
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> {
                Log.d("Debug", "Загрузка")
                Toast.makeText(requireContext(), "Загрузка", Toast.LENGTH_SHORT)
                    .show()
            }
            is AppState.Success -> {
                val serverData = appState.podDTO
                val url = serverData.url
                if (url.isNullOrEmpty()) {
                    Log.d("Debug", "URL пустой")
                    Toast.makeText(requireContext(), "Ошибка загрузки", Toast.LENGTH_LONG).show()
                } else {
                    Log.d("Debug", "Данные получены")
                    binding.imageDay.load(url)
                    setDataBottomSheet(serverData.title, serverData.explanation)
                }
            }
            is AppState.Error -> {
                Log.d("Debug", "Ошибка ${appState.throwable.message}")
                Toast.makeText(requireContext(), "${appState.throwable.message}", Toast.LENGTH_LONG)
                    .show()
            }
        }
    }

    private fun setBottomSheetBehavior(layout: FrameLayout) {
        bottomSheetBehavior = BottomSheetBehavior.from(layout)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED
    }

    private fun setDataBottomSheet(header: String?, desc: String?) {
        binding.includeBottomSheet.bottomSheetHeader.text =
            header ?: getString(R.string.header_bottom_sheet_default)
        binding.includeBottomSheet.bottomSheetText.text =
            desc ?: getString(R.string.desc_bottom_sheet_default)
    }
}