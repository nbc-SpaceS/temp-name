package com.wannabeinseoul.seoulpublicservice.adapter

import android.app.appsearch.SearchResult
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.databinding.CategoryItemBinding

class HomeSearchAdapter(private val items: MutableList<SearchResult>) : RecyclerView.Adapter<HomeSearchAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivImage: ImageView = binding.ivCtImage
        val tvPlaceName: TextView = binding.tvCtPlaceName
        val tvIsFree: TextView = binding.tvCtNotFree
        val tvReservationStatus: TextView = binding.tvCtReservationEnd
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CategoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        // TODO: ivImage에 이미지를 로드하는 코드를 추가
//        holder.tvPlaceName.text = item.placeName
//        holder.tvIsFree.text = if (item.isFree) "무료" else "유료"
//        holder.tvReservationStatus.text = if (item.isReservationEnd) "예약 종료" else "예약 가능"
    }

    override fun getItemCount() = items.size
}