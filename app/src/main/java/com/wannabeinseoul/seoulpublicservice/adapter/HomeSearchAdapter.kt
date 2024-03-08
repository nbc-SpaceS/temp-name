package com.wannabeinseoul.seoulpublicservice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databinding.CategoryItemBinding

class HomeSearchAdapter(val items: List<ReservationEntity>) : RecyclerView.Adapter<HomeSearchAdapter.ViewHolder>() {

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
        // ivImage에 coil 라이브러리를 사용하여 이미지 로드
        holder.ivImage.load(item.IMGURL)
        holder.tvPlaceName.text = item.PLACENM
        holder.tvIsFree.text = item.PAYATNM
        holder.tvReservationStatus.text = item.SVCSTATNM
    }

    override fun getItemCount() = items.size
}