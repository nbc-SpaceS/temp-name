package com.example.seoulpublicservice.ui.recommendation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.databases.ReservationEntity
import com.example.seoulpublicservice.databinding.CategoryItemBinding
import com.example.seoulpublicservice.seoul.SeoulDto
import com.squareup.picasso.Picasso


class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {

    private var items: List<ReservationEntity> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    fun setItems(newItems: List<ReservationEntity>) {
        items = newItems
        notifyDataSetChanged()
        Log.d("RecommendationAdapter", "Item count: ${items.size}")
    }

    inner class ViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReservationEntity) {
            binding.tvPlaceName.text = item.PLACENM
            binding.tvIsReservationAvailable.text = item.SVCSTATNM
            binding.tvPayType.text = item.PAYATNM
            binding.tvAreaName.text = item.AREANM

            Glide.with(binding.ivSmallImage.context)
                .load(item.IMGURL) // 예약 서비스의 이미지 URL
                .into(binding.ivSmallImage)
        }
    }
}