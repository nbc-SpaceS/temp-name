package com.wannabeinseoul.seoulpublicservice.ui.category

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wannabeinseoul.seoulpublicservice.databinding.CategoryItemBinding


class CategoryAdapter(private val onItemClick: (svcid: String) -> Unit) :
    ListAdapter<CategoryData, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {
    private lateinit var categoryData: CategoryData

    inner class CategoryViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryData) {
            binding.apply {
                binding.ivCtImage.load(item.imageUrl)
                binding.tvCtPlaceName.text = item.placeName
                binding.tvCtNotFree.text = item.payType
                binding.root.setOnClickListener { onItemClick(item.svcid) }
//apply 쓰고 binding 불필요
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

}

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryData>() {
    override fun areItemsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
        return oldItem.svcid == newItem.svcid
    }

    override fun areContentsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
        return oldItem == newItem
    }

}
