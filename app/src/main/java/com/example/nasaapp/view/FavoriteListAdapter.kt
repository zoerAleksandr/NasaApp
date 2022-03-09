package com.example.nasaapp.view

import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.RoundedCornersTransformation
import com.example.nasaapp.R
import com.example.nasaapp.databinding.ItemLayoutBinding
import com.example.nasaapp.model.PodDTO

class FavoriteListAdapter(
    private val dataList: MutableList<Pair<PodDTO, Boolean>>,
) : RecyclerView.Adapter<FavoriteListAdapter.ImageViewHolder>(),
    ItemTouchHelperAdapter {
    var removeItemCallback: RemoveItemCallback? = null

    companion object {
        fun newInstance() = FavoriteListAdapter(dataList = mutableListOf())
    }

    inner class ImageViewHolder(private val binding: ItemLayoutBinding) :
        RecyclerView.ViewHolder(binding.root), ItemTouchHelperViewHolder {

        private val zoom = 3
        private val paramsMin = binding.image.layoutParams
        private val paramsMax = binding.image.layoutParams
        private val maxWidth = paramsMax.width * zoom
        private val maxHeight = paramsMax.height * zoom
        val container = binding.root

        fun bind(data: Pair<PodDTO, Boolean>) {
            if (layoutPosition != RecyclerView.NO_POSITION) {
                binding.apply {
                    title.text = data.first.title
                    date.text = data.first.date
                    description.text = data.first.explanation
                    image.load(data.first.url) {
                        transformations(RoundedCornersTransformation(10f))
                    }
                    binding.description.visibility = if (data.second) View.VISIBLE else View.GONE

                    /* Следующее выражение может давать некорректный результат на эмуляторе,
                     лучше тестировать на реальном устройстве */
                    binding.image.layoutParams =
                        if (data.second) paramsMax.apply {
                            width = maxWidth
                            height = maxHeight
                        } else paramsMin

                }
                binding.root.setOnClickListener {
                    onListItemClickListener.onItemClick()
                }
            }
        }

        private val onListItemClickListener = object : OnListItemClickListener {
            override fun onItemClick() {
                toggleText()
            }
        }

        private fun toggleText() {
            dataList[layoutPosition] = dataList[layoutPosition].let {
                it.first to !it.second
            }
            notifyItemChanged(layoutPosition)
        }

        override fun onItemSelected() {
            itemView.setBackgroundColor(Color.LTGRAY)
        }

        override fun onItemClear() {
            // нечего делать
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLayoutBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(dataList[position])
        holder.container.animation =
            AnimationUtils.loadAnimation(
                holder.itemView.context,
                R.anim.animation_for_recycler_view
            )
    }

    override fun getItemCount() = dataList.size

    fun addList(newList: List<PodDTO>) {
        for (i in newList) {
            dataList.add(Pair(first = i, second = false))
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        dataList.removeAt(fromPosition).apply {
            dataList.add(if (toPosition > fromPosition) toPosition - 1 else toPosition, this)
        }
        Log.d("Debug", "$fromPosition $toPosition")
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        removeItemCallback?.remove(dataList[position].first)
        dataList.removeAt(position)
        notifyItemRemoved(position)
    }
}