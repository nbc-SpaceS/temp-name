package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemRecommendedBinding
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemRecommendedTipBinding

class RecommendationAdapter(private var items: List<MultiView>) :
    RecyclerView.Adapter<RecommendationAdapter.CommonViewHolder>() {

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

    abstract inner class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun onBind(item: MultiView)
    }

    inner class RecommendationViewHolder(
        private val binding: MyPageItemRecommendedBinding,
    ) : CommonViewHolder(binding.root) {

        override fun onBind(item: MultiView) {
            binding.reShared.adapter = (item as MultiView.Horizontal).adapter
            binding.tvSharedText.text = item.headerTitle


        }
    }

    inner class TipViewHolder(private val b: MyPageItemRecommendedTipBinding) :
        CommonViewHolder(b.root) {

        override fun onBind(item: MultiView) {
//                b.tvTitle.text = (item as MultiView.Tip).title
            b.tvTip.text = (item as MultiView.Tip).content
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].viewType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return when (MultiView.Type.values()[viewType]) {
            MultiView.Type.HORIZONTAL -> RecommendationViewHolder(
                MyPageItemRecommendedBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.TIP -> TipViewHolder(
                MyPageItemRecommendedTipBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.onBind(items[position])

    }

    fun submitList(newItems: List<MultiView>) {
        items = newItems
        notifyDataSetChanged()
    }
}