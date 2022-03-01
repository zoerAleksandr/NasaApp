package com.example.nasaapp.ui.main

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import by.kirich1409.viewbindingdelegate.viewBinding
import com.example.nasaapp.R
import com.example.nasaapp.databinding.SearchFragmentBinding

class SearchFragment: Fragment(R.layout.search_fragment) {

    private val binding: SearchFragmentBinding by viewBinding()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.textInputLayoutSearch.setStartIconOnClickListener {
            startActivity(Intent(Intent.ACTION_VIEW).apply {
                data =
                    Uri.parse("https://en.wikipedia.org/wiki/${binding.textSearch.text.toString()}")
            })
        }
    }
}