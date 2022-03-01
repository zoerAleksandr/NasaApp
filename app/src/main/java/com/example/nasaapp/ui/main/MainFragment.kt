package com.example.nasaapp.ui.main

import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import com.example.nasaapp.*
import com.example.nasaapp.databinding.MainFragmentBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout

class MainFragment : Fragment(R.layout.main_fragment) {

    companion object {
        fun newInstance() = MainFragment()

        const val EARTH = R.string.tab_earth
        const val MARS = R.string.tab_mars
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val binding: MainFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setBottomSheetBehavior(binding.includeBottomSheet.bottomSheetContainer)
        viewModel.getData(getToday()).observe(viewLifecycleOwner, { appState ->
            renderData(appState)
        })
        setAppBar()

        binding.apply {
            bottomAppBar.setNavigationOnClickListener {
                activity?.supportFragmentManager?.let { fragmentManager ->
                    NavigationViewFragment()
                        .show(
                            fragmentManager,
                            "tag"
                        )
                }
            }

            fab.setOnClickListener {
                binding.root.toast("Загружаю катинку дня")
            }

            chipGroup.setOnCheckedChangeListener { _, checkedId ->
                when (checkedId) {
                    R.id.chip_day_before -> {
                        viewModel.getData(getBeforeDay())
                    }
                    R.id.chip_yesterday -> {
                        viewModel.getData(getYesterday())
                    }
                    R.id.chip_today -> {
                        viewModel.getData(getToday())
                    }
                }
            }

            tabLayout.apply {
                addTab(tabLayout.newTab().setText(EARTH), 0, true)
                addTab(tabLayout.newTab().setText(MARS), 1, false)

                addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                    override fun onTabSelected(tab: TabLayout.Tab?) {
                        if (tab != null) {
                            when (tab.position) {
                                0 -> viewModel.resource = Resource.Earth
                                1 -> viewModel.resource = Resource.Mars
                            }
                        }
                        viewModel.getData(getToday())
                        chipGroup.check(R.id.chip_today)
                    }

                    override fun onTabUnselected(tab: TabLayout.Tab?) {
                        // нечего делать
                    }

                    override fun onTabReselected(tab: TabLayout.Tab?) {
                        // нечего делать
                    }

                })
            }
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> {
                setViewStateLoading(
                    binding.containerMainFragment,
                    binding.includeBottomSheet.root,
                    binding.shimmerContainer,
                    binding
                )
            }
            is AppState.Success -> {
                setViewStateSuccess(
                    binding.containerMainFragment,
                    binding.includeBottomSheet.root,
                    binding.shimmerContainer,
                    binding
                )

                when (val serverData = appState.podDTO) {
                    is PodDTO -> {
                        val url = serverData.url
                        if (url.isNullOrEmpty()) {
                            Log.d("Debug", "URL пустой")
                        } else {
                            binding.imageDay.load(url)
                            setDataBottomSheet(serverData.title, serverData.explanation)
                        }
                    }

                    is MarsPhotoListDTO -> {
                        val photos = serverData.photos
                        if (photos != null && photos.isNotEmpty()) {
                            Log.d("Debug", "${photos.size}")
                            binding.imageDay.load(photos[0].image)
                            setDataBottomSheet(serverData.photos[0].earthDate, "")
                        } else {
                            Toast.makeText(
                                requireContext(),
                                "Фото из Марса еще в пути",
                                Toast.LENGTH_LONG
                            ).show()
                            viewModel.getData(getYesterday())
                        }
                    }
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
            R.id.search_wiki -> {
                activity?.let {
                    it.supportFragmentManager.beginTransaction()
                        .replace(R.id.container, SearchFragment())
                        .addToBackStack("search")
                        .commit()
                }
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

// https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?&earth_date=2022-02-15&api_key=KFv7eZfX45YyqYvqsemofOOzMnzELbhSpfG1UX42
// https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=2022-2-15&api_key=DEMO_KEY

// https://api.nasa.gov/mars-photos/api/v1/rovers/curiosity/photos?earth_date=2022-02-15&api_key=KFv7eZfX45YyqYvqsemofOOzMnzELbhSpfG1UX42