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
    }

    override fun getItemCount() = items.size

    inner class ViewHolder(val textView: TextView) : RecyclerView.ViewHolder(textView)
}