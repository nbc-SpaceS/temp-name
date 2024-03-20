package com.wannabeinseoul.seoulpublicservice.ui.dialog.review

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wannabeinseoul.seoulpublicservice.databinding.ItemDetailCommentBinding

class ReviewAdapter(
    private val complaintUser: (String) -> Unit,
    private val deleteReview: (String) -> Unit,
    private val userId: String
) : ListAdapter<ReviewItem, ReviewAdapter.ReviewViewHolder>(object :
    DiffUtil.ItemCallback<ReviewItem>() {
    override fun areItemsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
        return oldItem.userId == newItem.userId
    }

    override fun areContentsTheSame(oldItem: ReviewItem, newItem: ReviewItem): Boolean {
        return oldItem == newItem
    }
}) {

    inner class ReviewViewHolder(
        private val binding: ItemDetailCommentBinding,
        private val complaintUser: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ReviewItem) = with(binding) {
            tvCommentText.text = item.content
            tvCommentUser.text = item.userName
            if (item.userProfileImage.isEmpty()) {
                ivCommentProfile.drawable.setTint(Color.parseColor(item.userColor))
            } else {
                ivCommentProfile.load(item.userProfileImage)
            }

            ivCommentDeleteBtn.isVisible = (userId == item.userId)

            clItemDetailComment.setOnLongClickListener {
                complaintUser(item.userId)
                true
            }

            if (item.reviewId == "") {
                ivCommentDeleteBtn.setOnClickListener(null)
            } else {
                ivCommentDeleteBtn.setOnClickListener {
                    deleteReview(item.reviewId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewViewHolder {
        return ReviewViewHolder(
            ItemDetailCommentBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            ),
            complaintUser = complaintUser
        )
    }

    override fun onBindViewHolder(holder: ReviewViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}