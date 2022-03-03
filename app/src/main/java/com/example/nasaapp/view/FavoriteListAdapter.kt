package com.example.nasaapp.view

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.nasaapp.databinding.ItemLayoutBinding
import com.example.nasaapp.model.PodDTO
import com.example.nasaapp.model.hide
import com.example.nasaapp.model.show


class FavoriteListAdapter : RecyclerView.Adapter<FavoriteListAdapter.ImageViewHolder>(),
    ItemTouchHelperAdapter {
    private var list: MutableList<PodDTO> = mutableListOf()
    private var itemDeployed = false

    companion object {
        fun newInstance() = FavoriteListAdapter()
    }

    inner class ImageViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {
        fun bind(podDTO: PodDTO) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.apply {
                    title.text = podDTO.title
                    date.text = podDTO.date
                    description.text = podDTO.explanation
                    image.load(podDTO.url)
                }
                binding.root.setOnClickListener {
                    OnListItemClickListener {
                        if (itemDeployed) binding.description.hide() else binding.description.show()
                        itemDeployed = !itemDeployed
                        notifyItemChanged(layoutPosition)
                    }
                }
            }
        }

        private fun removeItem() {
            list.removeAt(layoutPosition)
            notifyItemRemoved(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            itemView.setBackgroundColor(0)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount() = list.size

    fun addList(newList: List<PodDTO>) {
        list.addAll(newList)
        Log.d("Debug", "${list.size}")
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        list.removeAt(fromPosition).apply {
            list.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        list.removeAt(position)
        notifyItemRemoved(position)
    }

}