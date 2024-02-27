package com.example.seoulpublicservice.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.data.Item
import com.example.seoulpublicservice.databinding.ItemHomeBinding

class ItemAdapter(private val items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.icon)
        holder.name.text = item.name

        if (item.isSelected) {
            holder.icon.setBackgroundResource(R.drawable.background_radius_10dp_f8496c)
            holder.icon.setColorFilter(Color.WHITE)
        } else {
            holder.icon.setBackgroundResource(R.drawable.background_radius_10dp_dfdfdd)
            holder.icon.clearColorFilter()
        }

        holder.itemView.setOnClickListener {
            item.isSelected = !item.isSelected
            notifyItemChanged(position)
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon = binding.ivIcon
        val name = binding.tvName
    }
}