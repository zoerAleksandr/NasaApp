package com.example.nasaapp.view

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager.VERTICAL
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.nasaapp.R
import com.example.nasaapp.databinding.FavoriteFragmentBinding
import com.example.nasaapp.viewmodel.FavoriteListState
import com.example.nasaapp.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment(R.layout.favorite_fragment) {
    companion object {
        fun newInstance() = FavoriteFragment()
    }

    private val binding: FavoriteFragmentBinding by viewBinding()
    private val adapter = FavoriteListAdapter.newInstance()

    private val favoriteViewModel: FavoriteViewModel by lazy {
        ViewModelProvider(this)[FavoriteViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.recyclerView.adapter = adapter
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), VERTICAL, false)
        Log.d("Debug", "${adapter.itemCount}")

        favoriteViewModel.getData().observe(viewLifecycleOwner){ listState -> renderData(listState)}

        ItemTouchHelper(ItemTouchHelperCallback(adapter))
            .attachToRecyclerView(binding.recyclerView)
    }
    private fun renderData(listState: FavoriteListState){
        when(listState){
            is FavoriteListState.Success ->{
                adapter.addList(listState.listDTO)
            }
        }
    }
}