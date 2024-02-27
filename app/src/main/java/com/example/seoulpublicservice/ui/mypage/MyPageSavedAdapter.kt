package com.example.seoulpublicservice.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.databinding.CategoryItemBinding
import com.example.seoulpublicservice.seoul.Row

class MyPageSavedAdapter(
) : ListAdapter<Row, MyPageSavedAdapter.VH>(
    object : DiffUtil.ItemCallback<Row>() {
        override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean =
            oldItem.svcid == newItem.svcid

        override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean =
            oldItem == newItem
    }
) {

    inner class VH(private val b: CategoryItemBinding) :
        RecyclerView.ViewHolder(b.root) {

        fun onBind(item: Row) {
            if (item.imgurl.isBlank()) b.ivSmallVideoImage.load(R.drawable.place_holder_1)
            else b.ivSmallVideoImage.load(item.imgurl)
            b.tvRegion.text = item.areanm
            b.tvRegister.text = item.svcstatnm
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            CategoryItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position))
    }
}
