package com.wannabeinseoul.seoulpublicservice.ui.detail

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wannabeinseoul.seoulpublicservice.databinding.ItemDetailCommentBinding
import com.wannabeinseoul.seoulpublicservice.ui.dialog.review.ReviewItem
import com.wannabeinseoul.seoulpublicservice.util.parseColor

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
            if (detailCommentType.userProfileImage.isEmpty()) {
                binding.ivCommentProfile.drawable.setTint(detailCommentType.userColor.parseColor())
            } else {
                binding.ivCommentProfile.load(detailCommentType.userProfileImage)
            }
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