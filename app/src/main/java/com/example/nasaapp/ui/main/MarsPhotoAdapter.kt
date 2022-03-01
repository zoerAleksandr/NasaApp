package com.example.nasaapp.ui.main

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nasaapp.PodMarsPhotoDTO
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ItemPhotoBinding

class MarsPhotoAdapter : RecyclerView.Adapter<MarsPhotoAdapter.MarsPhotoViewHolder>() {
    companion object {
        fun newInstance() = MarsPhotoAdapter()
    }

    private var photos: List<PodMarsPhotoDTO> = listOf()
    private var myListener: MyListener? = null

    inner class MarsPhotoViewHolder(private val binding: ItemPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(marsPhotoDTO: PodMarsPhotoDTO) {
            binding.itemImage.load(marsPhotoDTO.image){
                placeholder(R.drawable.placeholder)
            }
            myListener?.newData(marsPhotoDTO.earthDate)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MarsPhotoViewHolder {
        val binding = ItemPhotoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MarsPhotoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MarsPhotoViewHolder, position: Int) {
        holder.bind(photos[position])
    }

    override fun getItemCount() = photos.size

    fun setData(photos: List<PodMarsPhotoDTO>){
        this.photos = photos
        notifyDataSetChanged()
    }
}

fun interface MyListener{
    fun newData(date: String?)
}