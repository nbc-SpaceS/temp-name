package com.wannabeinseoul.seoulpublicservice.ui.main.adapter

import android.content.Intent
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.data.Item
import com.wannabeinseoul.seoulpublicservice.databinding.ItemHomeBinding
import com.wannabeinseoul.seoulpublicservice.pref.RegionPrefRepository
import com.wannabeinseoul.seoulpublicservice.ui.category.CategoryActivity

class ItemAdapter(
    private val regionPrefRepository: RegionPrefRepository,
) : ListAdapter<Item, ItemAdapter.ItemViewHolder>(object : DiffUtil.ItemCallback<Item>() {
    override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem.name == newItem.name
    }

    override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
        return oldItem == newItem
    }

}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun getItemCount(): Int {
        return currentList.size
    }

    inner class ItemViewHolder(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) = with(binding) {
            ivIcon.setImageResource(item.icon)
            tvName.text = item.name

            ivIcon.setBackgroundResource(R.drawable.background_radius_10dp_ff6685)
            ivIcon.setColorFilter(Color.WHITE)

            if (item.count == 0) {
                // 리스트가 비어있을 때 아이템의 배경과 이미지 색을 변경하고 클릭 불가능하게 설정
                ivIcon.setBackgroundResource(R.drawable.background_radius_10dp_dfdfdd)
                ivIcon.setColorFilter(Color.GRAY)
                // 클릭 불가능하게 설정하려면 null을 넣어줘야 함
                itemView.setOnClickListener(null)
            } else {
                // 리스트가 비어있지 않을 때 아이템의 배경과 이미지 색을 원래대로 복원하고 클릭 가능하게 설정
                ivIcon.setBackgroundResource(R.drawable.background_radius_10dp_ff6685)
                ivIcon.setColorFilter(Color.WHITE)
                itemView.setOnClickListener {
                    if (regionPrefRepository.loadSelectedRegion() == "지역선택") {
                        Toast.makeText(it.context, "관심지역을 먼저 선택해주세요.", Toast.LENGTH_SHORT).show()
                    } else {
                        // 아이콘의 배경색과 색상을 변경
                        ivIcon.setBackgroundResource(R.drawable.background_radius_10dp_e95a77)
                        // 선택된 항목의 데이터와 지역을 가지고 카테고리 페이지로 이동
                        val intent = Intent(it.context, CategoryActivity::class.java).apply {
                            putExtra("category", item.name)
                            putExtra("region", regionPrefRepository.loadSelectedRegion())
                        }
                        Log.d(
                            "ItemAdapter",
                            "Moving to CategoryActivity with category: ${item.name}, region: ${regionPrefRepository.loadSelectedRegion()}"
                        )
                        it.context.startActivity(intent)
                        // 일정 시간 후에 아이콘의 배경색과 색상을 원래대로 복원
                        itemView.postDelayed({
                            ivIcon.setBackgroundResource(R.drawable.background_radius_10dp_ff6685)
                            ivIcon.setColorFilter(Color.WHITE)
                        }, 500)
                    }
                }
            }
        }
    }
}
