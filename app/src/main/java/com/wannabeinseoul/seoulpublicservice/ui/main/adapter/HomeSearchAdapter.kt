package com.wannabeinseoul.seoulpublicservice.ui.main.adapter

import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databinding.CategoryItemBinding

class HomeSearchAdapter(val items: List<ReservationEntity>) : RecyclerView.Adapter<HomeSearchAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: CategoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val ivImage: ImageView = binding.ivCtImage
        val tvServiceName: TextView = binding.tvCtServiceName
        val tvPlaceName: TextView = binding.tvCategoryItemPlace
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
        holder.tvServiceName.text = Html.fromHtml(item.SVCNM, Html.FROM_HTML_MODE_LEGACY)
        holder.tvPlaceName.text = item.PLACENM
        holder.tvIsFree.text = item.PAYATNM.take(2)
        holder.tvReservationStatus.text = item.SVCSTATNM

        if (item.PAYATNM.take(2) == "유료") {
            holder.tvIsFree.setTextColor(Color.parseColor("#5E5E5E"))
            holder.tvIsFree.setBackgroundResource(R.drawable.background_white_with_rounded_stroke)
        } else {
            holder.tvIsFree.setTextColor(Color.parseColor("#FFFFFF"))
            holder.tvIsFree.setBackgroundResource(R.drawable.background_pointcolor_with_rounded)
        }

        when (item.SVCSTATNM) {
            "접수중", "안내중" -> {
                holder.tvReservationStatus.setTextColor(Color.parseColor("#F8496C"))
                holder.tvReservationStatus.setBackgroundResource(R.drawable.background_white_with_f8496c_stroke)
            }

            else -> {
                holder.tvReservationStatus.setTextColor(Color.parseColor("#5E5E5E"))
                holder.tvReservationStatus.setBackgroundResource(R.drawable.background_white_with_rounded_stroke)
            }
        }
    }

    override fun getItemCount() = items.size
}