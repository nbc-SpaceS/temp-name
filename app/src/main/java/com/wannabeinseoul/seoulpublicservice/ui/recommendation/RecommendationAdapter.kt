package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.databinding.RecommendationItemRecommendedBinding
import com.wannabeinseoul.seoulpublicservice.databinding.RecommendationItemRecommendedTipBinding

class RecommendationAdapter :
    ListAdapter<RecommendationAdapter.MultiView, RecyclerView.ViewHolder>(DiffCallback()) {



    sealed interface MultiView {

        enum class Type {
            HORIZONTAL,
            TIP,
        }

        val viewType: Type

        data class Horizontal(
            val headerTitle: String,
            val adapter: RecommendationHorizontalAdapter,
        ) : MultiView {
            override val viewType: Type = Type.HORIZONTAL
        }

        data class Tip(
            val title: String,
            val content: String,
        ) : MultiView {
            override val viewType: Type = Type.TIP
        }
    }

    inner class RecommendationViewHolder(
        private val binding: RecommendationItemRecommendedBinding,
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MultiView.Horizontal) {
            binding.reShared.adapter = item.adapter
            binding.tvSharedText.text = item.headerTitle
        }
    }

    inner class TipViewHolder(private val binding: RecommendationItemRecommendedTipBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: MultiView.Tip) {
            binding.tvTipTitle.text = item.title
            binding.tvTipContent.text = item.content
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            MultiView.Type.HORIZONTAL.ordinal -> RecommendationViewHolder(
                RecommendationItemRecommendedBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.TIP.ordinal -> TipViewHolder(
                RecommendationItemRecommendedTipBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            else -> throw IllegalArgumentException("Invalid view type")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val item = getItem(position)
        when (item.viewType) {
            MultiView.Type.HORIZONTAL -> (holder as RecommendationViewHolder).bind(item as MultiView.Horizontal)
            MultiView.Type.TIP -> (holder as TipViewHolder).bind(item as MultiView.Tip)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return getItem(position).viewType.ordinal
    }

    class DiffCallback : DiffUtil.ItemCallback<MultiView>() {
        override fun areItemsTheSame(oldItem: MultiView, newItem: MultiView): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: MultiView, newItem: MultiView): Boolean {
            return oldItem == newItem
        }
    }
}