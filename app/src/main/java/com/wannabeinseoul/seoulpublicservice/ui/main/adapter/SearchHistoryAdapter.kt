package com.wannabeinseoul.seoulpublicservice.ui.main.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.databinding.ItemSearchHistoryBinding
import com.wannabeinseoul.seoulpublicservice.pref.SearchPrefRepository

class SearchHistoryAdapter(
    private var searchHistory: MutableList<String>,
    private val searchPrefRepository: SearchPrefRepository
) : RecyclerView.Adapter<SearchHistoryAdapter.ViewHolder>(){

    inner class ViewHolder(private val binding: ItemSearchHistoryBinding) : RecyclerView.ViewHolder(binding.root) {
        val tvSearchTerm: TextView = binding.tvSearchTerm
        val ivDelete: ImageView = binding.ivDelete
    }

    interface OnItemClickedListener {
        fun onItemClick(item: String)
    }

    var onItemClickListener: OnItemClickedListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemSearchHistoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = searchHistory[position]
        holder.tvSearchTerm.text = item
        holder.ivDelete.setOnClickListener {
            searchPrefRepository.delete(item)
            searchHistory = searchPrefRepository.load().toMutableList()
            notifyDataSetChanged()
        }
        holder.tvSearchTerm.setOnClickListener {
            onItemClickListener?.onItemClick(item)
        }
    }

    override fun getItemCount() = searchHistory.size

}