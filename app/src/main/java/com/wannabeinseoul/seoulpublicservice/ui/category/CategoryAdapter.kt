package com.wannabeinseoul.seoulpublicservice.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wannabeinseoul.seoulpublicservice.databinding.CategoryItemBinding


class CategoryAdapter(
    private val onItemClick: (String) -> Unit
) : RecyclerView.Adapter<CategoryAdapter.CategoryItemViewHolder>() {

    private var categoryList: List<CategoryData> = emptyList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryItemViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)
        return CategoryItemViewHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: CategoryItemViewHolder, position: Int) {
        val category = categoryList[position]
        holder.bind(category)
    }

    override fun getItemCount(): Int = categoryList.size

    fun submitList(newList: List<CategoryData>) {
        categoryList = newList
        notifyDataSetChanged()
    }

    inner class CategoryItemViewHolder(private val binding: CategoryItemBinding, onItemClick: (String) -> Unit) : RecyclerView.ViewHolder(binding.root) {

        init {
            binding.root.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val category = categoryList[position]
                    onItemClick(category.svcid)
                }
            }
        }

        fun bind(category: CategoryData) {
            binding.apply {
                ivCtImage.load(category.imageUrl)
                tvCtPlaceName.text = category.placeName
                tvCtNotFree.text = category.payType
            }
        }
    }
}