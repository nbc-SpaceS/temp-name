package com.example.seoulpublicservice.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.seoulpublicservice.databinding.ItemSelectedOptionBinding

class MapOptionAdapter : ListAdapter<String, MapOptionAdapter.OptionViewHolder>(object :
    DiffUtil.ItemCallback<String>() {
    override fun areItemsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: String, newItem: String): Boolean {
        return oldItem == newItem
    }

}) {
    abstract class OptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(item: String)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
        return SelectedOptionViewHolder(
            binding = ItemSelectedOptionBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class SelectedOptionViewHolder(private val binding: ItemSelectedOptionBinding) :
        OptionViewHolder(binding.root) {
        override fun onBind(item: String) {
            binding.tvSelectedOptionItem.text = item
        }
    }
}