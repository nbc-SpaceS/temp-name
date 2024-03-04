package com.wannabeinseoul.seoulpublicservice.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.databinding.ItemDetailCommentBinding

class DetailCommentAdapter:ListAdapter<DetailCommentType, DetailCommentAdapter.Holder>(object : DiffUtil.ItemCallback<DetailCommentType>() {
    override fun areItemsTheSame(oldItem: DetailCommentType, newItem: DetailCommentType): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: DetailCommentType,
        newItem: DetailCommentType
    ): Boolean {
        return oldItem == newItem
    }
}) {
    inner class Holder(val binding: ItemDetailCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(detailCommentType: DetailCommentType) {
            binding.tvCommentUser.text = detailCommentType.name
            binding.tvCommentText.text = detailCommentType.text
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemDetailCommentBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
    }
}