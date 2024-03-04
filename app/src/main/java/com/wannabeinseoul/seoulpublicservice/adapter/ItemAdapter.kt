package com.wannabeinseoul.seoulpublicservice.adapter

import android.content.Intent
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.data.Item
import com.wannabeinseoul.seoulpublicservice.databinding.ItemHomeBinding
import com.wannabeinseoul.seoulpublicservice.ui.category.CategoryActivity

class ItemAdapter(private var items: List<Item>) : RecyclerView.Adapter<ItemAdapter.ItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val binding = ItemHomeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val item = items[position]
        holder.icon.setImageResource(item.icon)
        holder.name.text = item.name

        holder.icon.setBackgroundResource(R.drawable.background_radius_10dp_ff6685)
        holder.icon.setColorFilter(Color.WHITE)

        holder.itemView.setOnClickListener {
            // 아이콘의 배경색과 색상을 변경
            holder.icon.setBackgroundResource(R.drawable.background_radius_10dp_e95a77)


            // 선택된 항목의 데이터를 가지고 카테고리 페이지로 이동
            val intent = Intent(it.context, CategoryActivity::class.java).apply {
                putExtra("category", item.name)
            }
            it.context.startActivity(intent)

            // 일정 시간 후에 아이콘의 배경색과 색상을 원래대로 복원
            holder.itemView.postDelayed({
                holder.icon.setBackgroundResource(R.drawable.background_radius_10dp_ff6685)
                holder.icon.setColorFilter(Color.WHITE)
            }, 500)
        }

//        // 아이템이 클릭되지 않았을 때 아이콘의 배경색과 색상을 원래대로 설정
//        holder.icon.setBackgroundResource(R.drawable.background_radius_10dp_dfdfdd)
//        holder.icon.clearColorFilter()
    }

    override fun getItemCount(): Int {
        return items.size
    }

    inner class ItemViewHolder(val binding: ItemHomeBinding) : RecyclerView.ViewHolder(binding.root) {
        val icon = binding.ivIcon
        val name = binding.tvName
    }
}