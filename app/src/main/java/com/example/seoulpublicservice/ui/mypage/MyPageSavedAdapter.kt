package com.example.seoulpublicservice.ui.mypage

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.databinding.CategoryItemBinding
import com.example.seoulpublicservice.seoul.Row
import com.example.seoulpublicservice.util.loadWithHolder

class MyPageSavedAdapter(
) : ListAdapter<Row?, MyPageSavedAdapter.VH>(
    object : DiffUtil.ItemCallback<Row?>() {
        // TODO: null로 하려니까 변경 시 애니메이션이 이상해진다. 따로 데이터 클래스 만들어서 써야할듯. 서비스아이디랑 같이.
        override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean =
            oldItem.svcid == newItem.svcid

        override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean =
            oldItem == newItem
    }
) {

    inner class VH(private val b: CategoryItemBinding) :
        RecyclerView.ViewHolder(b.root) {

        init {
            b.root.setOnClickListener {}
        }

        fun onBind(item: Row?) {
            if (item == null) {
                // TODO: 레이아웃에서 '삭제된 서비스입니다' 띄우는거 겹쳐놓고 gone으로 놨다가 띄워야 할 듯.

                b.ivSmallVideoImage.load(R.drawable.place_holder_1)
                b.tvRegion.text = null
                b.tvRegister.text = null
            } else {
                b.ivSmallVideoImage.loadWithHolder(item.imgurl)
                b.tvRegion.text = item.areanm
                b.tvRegister.text = item.svcstatnm
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        return VH(
            CategoryItemBinding
                .inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        holder.onBind(getItem(position))
    }
}
