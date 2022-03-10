package com.example.nasaapp.view

import android.os.Bundle
import android.text.Spannable
import android.text.SpannableString
import android.text.style.TextAppearanceSpan
import android.util.Log
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.animation.AnticipateOvershootInterpolator
import android.view.animation.OvershootInterpolator
import android.widget.FrameLayout
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView.HORIZONTAL
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import by.kirich1409.viewbindingdelegate.viewBinding
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.nasaapp.R
import com.example.nasaapp.databinding.MainFragmentBinding
import com.example.nasaapp.model.*
import com.example.nasaapp.viewmodel.AppState
import com.example.nasaapp.viewmodel.FavoriteViewModel
import com.example.nasaapp.viewmodel.MainViewModel
import com.example.nasaapp.viewmodel.SelectedTab
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.tabs.TabLayout
import com.google.android.youtube.player.YouTubeIntents

class MainFragment : Fragment(R.layout.main_fragment), MyListener {

    companion object {
        fun newInstance() = MainFragment()

        const val EARTH = R.string.tab_earth
        const val MARS = R.string.tab_mars
    }

    private lateinit var viewModel: MainViewModel
    private lateinit var bottomSheetBehavior: BottomSheetBehavior<FrameLayout>
    private val binding: MainFragmentBinding by viewBinding()
    private val adapter = MarsPhotoAdapter.newInstance()
    private var fullScreen = false
    private var searchVisible = false
    private var currentPOD: PodDTO? = null
    private val favoriteViewModel: FavoriteViewModel by lazy {
        ViewModelProvider(this)[FavoriteViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[MainViewModel::class.java]
        setBottomSheetBehavior(binding.includeBottomSheet.bottomSheetContainer)
        viewModel.getData(getToday()).observe(viewLifecycleOwner) { appState ->
            renderData(appState)
        }
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
                    R.id.chip_video -> {
                        viewModel.getData("2022-02-16")
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
                                0 -> {
                                    viewModel.resource = SelectedTab.Earth
                                    binding.chipVideo.show()
                                }
                                1 -> {
                                    viewModel.resource = SelectedTab.Mars
                                    binding.chipVideo.hide()
                                }
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

            recyclerView.adapter = adapter
            recyclerView.layoutManager = LinearLayoutManager(requireContext(), HORIZONTAL, false)

            btnReload.setOnClickListener {
                viewModel.getData(getYesterday())
            }

            btnBackInputLayout.setOnClickListener {
                hideSearch()
            }

            imageDay.setOnClickListener {
                changeImageSize(containerMainFragment, imageDay, fullScreen)
                slideDown(main, includeBottomSheet.root, fullScreen)
                fullScreen = !fullScreen
            }
        }
    }
// !!! Вопрос прподавателю !!!
// Когда устонавливаю текст в TabItem через данный метод и в стиле указываю загруженный шрифт в формате ttf,
// то все работает (шрифт применяется). Но если в стиле указать загружаемый шрифт, то он не применится.
// Вопрос: как пользоваться загружаемым шрифтом в стилях?
// P.S. временно решил вопрос указав шрифт в TabLayout xml
    private fun setTextTabOne(): SpannableString {
        return SpannableString("Фото дня").apply {
            this.setSpan(
                TextAppearanceSpan(context, R.style.StyleForTabs),
                0, this.length,
                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
            )
        }
    }

    private fun renderData(appState: AppState) {
        when (appState) {
            is AppState.Loading -> {
                binding.errorLayout.hide()
                setViewStateLoading(
                    binding.containerMainFragment,
                    binding.includeBottomSheet.root,
                    binding.shimmerContainer,
                    binding
                )
            }
            is AppState.Success -> {
                binding.errorLayout.hide()
                setViewStateSuccess(
                    binding.containerMainFragment,
                    binding.includeBottomSheet.root,
                    binding.shimmerContainer,
                    binding
                )

                when (val serverData = appState.podDTO) {
                    is PodDTO -> {
                        when (serverData.mediaType) {
                            "image" -> {
                                setImagePod(serverData)
                                currentPOD = serverData
                            }
                            "video" -> {
                                setDataVideo(serverData)
                            }
                        }
                    }

                    is MarsPhotoListDTO -> {
                        setImageMarsPhoto(serverData.photos)
                    }
                }
            }
            is AppState.Error -> {
                Log.d("Debug", "Ошибка ${appState.throwable.message}")
                binding.apply {
                    shimmerContainer.stopShimmer()
                    shimmerContainer.hide()
                    playVideo.hide()
                    errorLayout.show()
                    textError.text = appState.throwable.message
                }
            }
        }
    }

    private fun setDataVideo(data: PodDTO) {
        val videoId = data.url?.split("embed/", "?rel=0")?.get(1)
        binding.apply {
            imageDay.load("https://img.youtube.com/vi/$videoId/maxresdefault.jpg") {
                transformations(RoundedCornersTransformation(10f))
            }
            playVideo.show()
            playVideo.setOnClickListener {
                val intent =
                    YouTubeIntents.createPlayVideoIntent(requireContext(), videoId.toString())
                startActivity(intent)
            }
        }
        setDataBottomSheet(data.title, data.explanation)
    }

    private fun setImagePod(data: PodDTO) {
        if (!(data.url == null || data.url.isEmpty())) {
            binding.apply {
                recyclerView.hide()
                playVideo.hide()
                imageDay.show()
                imageDay.load(data.url) {
                    transformations(RoundedCornersTransformation(10f))
                }
            }
            setDataBottomSheet(data.title, data.explanation)
        } else {
            Log.d("Debug", "URL пустой")
            viewModel.getData(getYesterday())
            binding.chipGroup.check(R.id.chip_yesterday)
        }
    }

    private fun setImageMarsPhoto(photos: List<PodMarsPhotoDTO>?) {
        if (!(photos == null || photos.isEmpty())) {
            binding.apply {
                imageDay.hide()
                recyclerView.show()
            }
            adapter.setData(photos)
            setDataBottomSheet(photos[0].earthDate, "")
        } else {
            Toast.makeText(
                requireContext(),
                "Фото из Марса еще в пути",
                Toast.LENGTH_LONG
            ).show()
            viewModel.getData(getYesterday())
            binding.chipGroup.check(R.id.chip_yesterday)
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
                saveToFavorite()
            }
            R.id.menu_appbar_profile -> {
//                binding.root.toast("Открыл профиль")
                if (binding.myCustomView.isVisible){
                    binding.myCustomView.hide()
                }else binding.myCustomView.show()
            }
            R.id.search_wiki -> {
                if (searchVisible) hideSearch() else showSearch()
                searchVisible = !searchVisible
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun saveToFavorite() {
        if (currentPOD != null) {
            favoriteViewModel.insertData(currentPOD!!)
            binding.bottomAppBar.menu.findItem(R.id.menu_appbar_favorite)
                .setIcon(R.drawable.ic_favorite_24)
            binding.root.toast("Добавлено в избранные")
        } else {
            binding.root.toast("Данные не загружены")
        }
    }

    private fun showSearch() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.main_fragment_search)

        val transition = ChangeBounds().also {
            it.duration = 1200
            it.interpolator = AnticipateOvershootInterpolator(0.5f)
        }
        TransitionManager.beginDelayedTransition(binding.containerMainFragment, transition)
        constraintSet.applyTo(binding.containerMainFragment)
    }

    private fun hideSearch() {
        val constraintSet = ConstraintSet()
        constraintSet.clone(requireContext(), R.layout.main_fragment_container)

        val transition = ChangeBounds().also {
            it.duration = 1200
            it.interpolator = OvershootInterpolator(0.5f)
        }
        TransitionManager.beginDelayedTransition(binding.containerMainFragment, transition)
        constraintSet.applyTo(binding.containerMainFragment)
    }

    private fun setAppBar() {
        val context = activity as MainActivity
        context.setSupportActionBar(binding.bottomAppBar)
        setHasOptionsMenu(true)
    }

    override fun newData(date: String?) {
        setDataBottomSheet(date, "")
    }
}