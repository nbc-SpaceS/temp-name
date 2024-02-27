package com.example.seoulpublicservice.ui.mypage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.seoulpublicservice.R
import com.example.seoulpublicservice.databinding.MyPageItemProfileBinding
import com.example.seoulpublicservice.databinding.MyPageItemReviewedBinding
import com.example.seoulpublicservice.databinding.MyPageItemReviewedHeaderBinding
import com.example.seoulpublicservice.databinding.MyPageItemSavedBinding
import com.example.seoulpublicservice.seoul.Row

class MyPageAdapter(
) : ListAdapter<MyPageAdapter.MultiView, MyPageAdapter.CommonViewHolder>(
    object : DiffUtil.ItemCallback<MultiView>() {
        override fun areItemsTheSame(oldItem: MultiView, newItem: MultiView): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: MultiView, newItem: MultiView): Boolean =
            oldItem == newItem
    }
) {

    sealed interface MultiView {

        enum class Type {
            PROFILE,
            SAVED,
            REVIEWED_HEADER,
            REVIEWED,
//            LOADING,
        }

        val viewType: Type

        data object Profile : MultiView {
            override val viewType: Type = Type.PROFILE
        }

        data class Saved(
            val myPageSavedAdapter: MyPageSavedAdapter
        ) : MultiView {
            override val viewType: Type = Type.SAVED
        }

        data object ReviewedHeader : MultiView {
            override val viewType: Type = Type.REVIEWED_HEADER
        }

        data class Reviewed(
            val row: Row
        ) : MultiView {
            override val viewType: Type = Type.REVIEWED
        }

//        data object Loading : MultiView {
//            override val viewType: Type = Type.LOADING
//        }

    }

    abstract inner class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun onBind(item: MultiView)
    }

    inner class ProfileHolder(private val b: MyPageItemProfileBinding) :
        CommonViewHolder(b.root) {
        override fun onBind(item: MultiView) {
        }
    }

    inner class SavedHolder(private val b: MyPageItemSavedBinding) :
        CommonViewHolder(b.root) {
        private lateinit var adapter: MyPageSavedAdapter

        override fun onBind(item: MultiView) {
            if (this::adapter.isInitialized.not()) {
                b.rvSaved.adapter = (item as MultiView.Saved).myPageSavedAdapter
                adapter = item.myPageSavedAdapter
            }
        }
    }

    inner class ReviewedHeaderHolder(b: MyPageItemReviewedHeaderBinding) :
        CommonViewHolder(b.root) {
        override fun onBind(item: MultiView) {}
    }

    inner class ReviewedHolder(private val b: MyPageItemReviewedBinding) :
        CommonViewHolder(b.root) {
        init {
            b.root.setOnClickListener {
            }
        }

        override fun onBind(item: MultiView) {
            val row = (item as MultiView.Reviewed).row
            if (row.imgurl.isBlank()) b.ivReviewedThumbnail.load(R.drawable.place_holder_1)
            else b.ivReviewedThumbnail.load(row.imgurl)
            b.tvReviewedArea.text = row.areanm
            b.tvReviewedTitle.text = row.svcnm
            b.tvReviewedReviewContent.text = "댓글 단 내용은 이런 식으로~"
            b.tvReviewedDate.text = "12-34-56"
        }
    }

//    inner class LoadingHolder(private val b: ItemLoadingProgressBinding) :
//        CommonViewHolder(b.root) {
//        override fun onBind(item: MultiView) {
////            Log.d("jj-LoadingHolder onBind", "${b.root}")
//            b.root.isVisible = itemCount > 4
//        }
//    }

    override fun getItemViewType(position: Int): Int = getItem(position).viewType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return when (MultiView.Type.values()[viewType]) {
            MultiView.Type.REVIEWED_HEADER -> ReviewedHeaderHolder(
                MyPageItemReviewedHeaderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.PROFILE -> ProfileHolder(
                MyPageItemProfileBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.SAVED -> SavedHolder(
                MyPageItemSavedBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.REVIEWED -> ReviewedHolder(
                MyPageItemReviewedBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

//            MultiView.Type.LOADING -> LoadingHolder(
//                ItemLoadingProgressBinding
//                    .inflate(LayoutInflater.from(parent.context), parent, false)
//            )
        }
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }
}
