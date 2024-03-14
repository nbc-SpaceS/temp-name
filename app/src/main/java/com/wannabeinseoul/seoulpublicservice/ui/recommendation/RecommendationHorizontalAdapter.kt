package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wannabeinseoul.seoulpublicservice.databinding.RecommendationItemBinding

class RecommendationHorizontalAdapter(
    private val recommendationDataList: MutableList<RecommendationData>, // 변경된 부분
    private val onItemClick: (RecommendationData) -> Unit
) : ListAdapter<RecommendationData, RecommendationHorizontalAdapter.VH>(DiffCallback()) {

    inner class VH(private val binding: RecommendationItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecommendationData) {
            binding.ivRcSmallImage.load(item.imageUrl)
            binding.tvRcPlaceName.text = item.placeName
            binding.tvRcPayType.text = item.payType
            binding.tvRcAreaName.text = item.areaName
            binding.tvRcReview.text = "후기 ${item.reviewCount}개"
            binding.root.setOnClickListener { onItemClick(item) }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            RecommendationItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.bind(getItem(position))
    }

    class DiffCallback : DiffUtil.ItemCallback<RecommendationData>() {
        override fun areItemsTheSame(
            oldItem: RecommendationData,
            newItem: RecommendationData
        ): Boolean {
            return oldItem.svcid == newItem.svcid
        }

        override fun areContentsTheSame(
            oldItem: RecommendationData,
            newItem: RecommendationData
        ): Boolean {
            return oldItem == newItem
        }
    }
}