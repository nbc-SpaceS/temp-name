package com.wannabeinseoul.seoulpublicservice.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class SearchHistoryAdapter(private val items: List<String>) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>(){

    interface OnItemClickedListener {
        fun onItemClick(item: String)
    }

    var onItemClickListener: OnItemClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val textView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false) as TextView
        return ViewHolder(textView)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.textView.text = item
        holder.itemView.setOnClickListener { onItemClickListener?.onItemClick(item) }
//        holder.deleteButton.setOnClickListener {
//            // 삭제 버튼을 클릭하면 해당 항목을 삭제
//            items.removeAt(position)
//            notifyItemRemoved(position)
//            // TODO: SearchPrefRepository에서 해당 항목을 삭제하는 코드를 추가
//        }
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}