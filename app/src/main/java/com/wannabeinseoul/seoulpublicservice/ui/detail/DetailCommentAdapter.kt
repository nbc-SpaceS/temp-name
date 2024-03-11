package com.wannabeinseoul.seoulpublicservice.ui.detail

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.databinding.ItemDetailCommentBinding
import com.wannabeinseoul.seoulpublicservice.dialog.review.ReviewItem

class DetailCommentAdapter:ListAdapter<ReviewItem, DetailCommentAdapter.Holder>(object : DiffUtil.ItemCallback<ReviewItem>() {
    override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
        return oldItem == newItem
    }

    override fun areContentsTheSame(
        oldItem: ReviewItem,
        newItem: ReviewItem
    ): Boolean {
        return oldItem == newItem
    }
}) {
    inner class Holder(val binding: ItemDetailCommentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(detailCommentType: ReviewItem) {
            binding.tvCommentUser.text = detailCommentType.userName
            binding.tvCommentText.text = detailCommentType.content
            binding.ivCommentProfile.drawable.setTint(Color.parseColor(detailCommentType.userColor))
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