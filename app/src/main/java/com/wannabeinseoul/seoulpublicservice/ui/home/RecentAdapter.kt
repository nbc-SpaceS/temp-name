package com.wannabeinseoul.seoulpublicservice.ui.home

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.databases.RecentEntity
import com.wannabeinseoul.seoulpublicservice.databinding.ItemHomeRecentBinding
import com.wannabeinseoul.seoulpublicservice.ui.category.CategoryItemClick
import com.wannabeinseoul.seoulpublicservice.util.loadWithHolder


class RecentAdapter: ListAdapter<RecentEntity, RecentAdapter.Holder>(object : DiffUtil.ItemCallback<RecentEntity>() {
    override fun areItemsTheSame(oldItem: RecentEntity, newItem: RecentEntity): Boolean {
        return oldItem.DATETIME == newItem.DATETIME
    }

    override fun areContentsTheSame(oldItem: RecentEntity, newItem: RecentEntity): Boolean {
        return oldItem == newItem
    }
}) {
    inner class Holder(val binding: ItemHomeRecentBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(data: RecentEntity) {
            binding.ivHomeRecentImg.loadWithHolder(data.IMGURL)
            binding.tvHomeRecentServiceName.text = Html.fromHtml(data.SVCNM, Html.FROM_HTML_MODE_LEGACY)
            binding.tvHomeRecentAreaMinclassnm.text = "${data.AREANM} - ${data.MINCLASSNM}"
            binding.tvHomeRecentPay.text = data.PAYATNM
            binding.tvHomeRecentSvcstatnm.text = data.SVCSTATNM
        }
        fun click(data: RecentEntity) {
            binding.root.setOnClickListener {
                itemClick?.onClick(data.SVCID)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        return Holder(ItemHomeRecentBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.bind(getItem(position))
        holder.click(getItem(position))
    }

    var itemClick: CategoryItemClick? = null
}   // Interface로 클릭된 페이지로 이동 해야함