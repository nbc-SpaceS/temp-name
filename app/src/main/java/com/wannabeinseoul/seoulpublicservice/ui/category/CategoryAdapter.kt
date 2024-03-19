package com.wannabeinseoul.seoulpublicservice.ui.category

import android.graphics.Color
import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databinding.ItemCategoryBinding


class CategoryAdapter(private val onItemClick: (svcid: String) -> Unit) :
    ListAdapter<CategoryData, CategoryAdapter.CategoryViewHolder>(CategoryDiffCallback()) {
    private lateinit var categoryData: CategoryData

    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CategoryData) {
            binding.apply {
                binding.ivCtImage.load(item.imageUrl)
                binding.tvCtServiceName.text = Html.fromHtml(item.serviceName, Html.FROM_HTML_MODE_LEGACY)
                binding.tvCategoryItemPlace.text = item.placeName
                binding.tvCtReservationEnd.text = item.isReservationAvailable
                when (item.isReservationAvailable) {
                    "접수중", "안내중" -> {
                        binding.tvCtReservationEnd.setTextColor(Color.parseColor("#F8496C"))
                        binding.tvCtReservationEnd.setBackgroundResource(R.drawable.background_white_with_f8496c_stroke)
                    }

                    else -> {
                        binding.tvCtReservationEnd.setTextColor(Color.parseColor("#5E5E5E"))
                        binding.tvCtReservationEnd.setBackgroundResource(R.drawable.background_white_with_rounded_stroke)
                    }
                }
                binding.tvCtNotFree.text = item.payType.take(2)
                if (item.payType.take(2) == "유료") {
                    binding.tvCtNotFree.setTextColor(Color.parseColor("#5E5E5E"))
                    binding.tvCtNotFree.setBackgroundResource(R.drawable.background_white_with_rounded_stroke)
                } else {
                    binding.tvCtNotFree.setTextColor(Color.parseColor("#FFFFFF"))
                    binding.tvCtNotFree.setBackgroundResource(R.drawable.background_pointcolor_with_rounded)
                }
                binding.root.setOnClickListener { onItemClick(item.svcid) }
//apply 쓰고 binding 불필요
            }
        }
        fun click(svcID: String) {
            binding.ivSmallVideoItems.setOnClickListener {
                categoryItemClick?.onClick(svcID)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val binding =
            ItemCategoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CategoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
        holder.click(item.svcid)
    }

    var categoryItemClick: CategoryItemClick? = null
}

class CategoryDiffCallback : DiffUtil.ItemCallback<CategoryData>() {
    override fun areItemsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
        return oldItem.svcid == newItem.svcid
    }

    override fun areContentsTheSame(oldItem: CategoryData, newItem: CategoryData): Boolean {
        return oldItem == newItem
    }

}

// 카테고리 아이템 클릭 시 상세 페이지로 SVCID를 전달하기 위한 인터페이스
interface CategoryItemClick {
    fun onClick(svcID: String)
}