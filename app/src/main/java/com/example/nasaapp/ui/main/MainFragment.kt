package com.example.nasaapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.nasaapp.AppState
import com.example.nasaapp.MainActivity
import com.example.nasaapp.R
import com.example.nasaapp.databinding.MainFragmentBinding
import com.example.nasaapp.toast
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
        setAppBar()

        binding.textInputLayoutSearch.setStartIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.textSearch.text.toString()}")
            })
        }

        binding.bottomAppBar.setNavigationOnClickListener {
            activity?.supportFragmentManager?.let { fragmentManager ->
                NavigationViewFragment().show(
                    fragmentManager,
                    "tag"
                )
            }
        }

        binding.fab.setOnClickListener {
            binding.root.toast("Загружаю катинку дня")
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> {
                Log.d("Debug", "Загрузка")
                binding.root.toast("Загрузка")
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
                binding.root.toast("${appState.throwable.message}")
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

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        super.onCreateOptionsMenu(menu, inflater)
        inflater.inflate(R.menu.menu_appbar, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_appbar_favorite -> {
                binding.root.toast("Добавил в избранные")
            }
            R.id.menu_appbar_profile -> {
                binding.root.toast("Открыл профиль")
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
    }

}