package com.wannabeinseoul.seoulpublicservice.ui.mypage

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserEntity
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemProfileBinding
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemReviewedBinding
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemReviewedHeaderBinding
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemReviewedNothingBinding
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemSavedBinding
import com.wannabeinseoul.seoulpublicservice.util.loadWithHolder
import com.wannabeinseoul.seoulpublicservice.util.parseColor

class MyPageAdapter(
    private val onClearClick: () -> Unit,
    private val onReviewedClick: (svcid: String) -> Unit,
) : ListAdapter<MyPageAdapter.MultiView, MyPageAdapter.CommonViewHolder>(
    object : DiffUtil.ItemCallback<MultiView>() {
        override fun areItemsTheSame(oldItem: MultiView, newItem: MultiView): Boolean =
            oldItem === newItem

        override fun areContentsTheSame(oldItem: MultiView, newItem: MultiView): Boolean =
            oldItem == newItem
    }
) {

    /** 멀티뷰 sealed interface */
    sealed interface MultiView {

        enum class Type {
            PROFILE,
            SAVED,
            REVIEWED_HEADER,
            REVIEWED,
            REVIEWED_NOTHING,
//            LOADING,
        }

        val viewType: Type

        data class Profile(
            val userEntity: UserEntity?,
            val onEditButtonClick: () -> Unit,
        ) : MultiView {
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
            val reviewedData: ReviewedData
        ) : MultiView {
            override val viewType: Type = Type.REVIEWED
        }

        data object ReviewedNothing : MultiView {
            override val viewType: Type = Type.REVIEWED_NOTHING
        }

//        data object Loading : MultiView {
//            override val viewType: Type = Type.LOADING
//        }

    }


    /** 뷰홀더들 */
    abstract inner class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun onBind(item: MultiView)
    }

    inner class ProfileHolder(private val b: MyPageItemProfileBinding) :
        CommonViewHolder(b.root) {

        private var isNotInitialized = true

        override fun onBind(item: MultiView) {
            if (isNotInitialized) {
                (item as MultiView.Profile)
                b.clProfileEdit.setOnClickListener {
                    item.onEditButtonClick()
                }
                item.userEntity?.let { user ->
                    b.tvProfileNickname.text = user.userName
                    b.ivProfileProfile.drawable.setTint(user.userColor?.parseColor() ?: 0
                        .apply {
                            Log.e(
                                "jj-마이페이지 어댑터",
                                "parseColor == null. userColor: ${user.userColor}"
                            )
                        }
                    )
                    if (user.userProfileImage.isNullOrBlank().not())
                        b.ivProfileProfile.loadWithHolder(user.userProfileImage)
                }
                    ?: {
                        Log.e(
                            "jj-마이페이지 어댑터",
                            "ProfileHolder - item.userEntity == null"
                        )
                    }
                isNotInitialized = false
            }
        }
    }

    inner class SavedHolder(private val b: MyPageItemSavedBinding) :
        CommonViewHolder(b.root) {

        init {
            b.tvSavedClear.setOnClickListener { onClearClick() }
        }

        private var isAdapterNotBound = true

        override fun onBind(item: MultiView) {
            item as MultiView.Saved
            if (isAdapterNotBound) {
                b.rvSaved.adapter = item.myPageSavedAdapter
                isAdapterNotBound = false
            }

            // TODO: onBind에서 하면 전체삭제 했을 때 안바뀜.
            //  프래그먼트에서 옵저빙해서 바꿔야. 호출을 거기서 만드려면 람다식으로는 불가.
            //  그냥 텍뷰를 public으로 뽑아서 만져야하나..?
            b.tvSavedNothing.isVisible = item.myPageSavedAdapter.itemCount == 0
        }
    }

    inner class ReviewedHeaderHolder(b: MyPageItemReviewedHeaderBinding) :
        CommonViewHolder(b.root) {
        override fun onBind(item: MultiView) {}
    }

    inner class ReviewedHolder(private val b: MyPageItemReviewedBinding) :
        CommonViewHolder(b.root) {

        override fun onBind(item: MultiView) {
            val reviewedData = (item as MultiView.Reviewed).reviewedData
            val row = item.reviewedData.row
            b.ivReviewedThumbnail.loadWithHolder(row.imgurl)
            b.tvReviewedArea.text = row.areanm
            b.tvReviewedTitle.text = row.svcnm
            b.tvReviewedReviewContent.text = reviewedData.content
            b.tvReviewedDate.text = reviewedData.uploadTime

            b.root.setOnClickListener { onReviewedClick(row.svcid) }
        }
    }

    inner class ReviewedNothingHolder(b: MyPageItemReviewedNothingBinding) :
        CommonViewHolder(b.root) {
        override fun onBind(item: MultiView) {}
    }

//    inner class LoadingHolder(private val b: ItemLoadingProgressBinding) :
//        CommonViewHolder(b.root) {
//        override fun onBind(item: MultiView) {
////            Log.d("jj-LoadingHolder onBind", "${b.root}")
//            b.root.isVisible = itemCount > 4
//        }
//    }

    /* 뷰홀더 끝 */


    override fun getItemViewType(position: Int): Int = getItem(position).viewType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return when (MultiView.Type.values()[viewType]) {
            MultiView.Type.PROFILE -> ProfileHolder(
                MyPageItemProfileBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.SAVED -> SavedHolder(
                MyPageItemSavedBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.REVIEWED_HEADER -> ReviewedHeaderHolder(
                MyPageItemReviewedHeaderBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.REVIEWED -> ReviewedHolder(
                MyPageItemReviewedBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.REVIEWED_NOTHING -> ReviewedNothingHolder(
                MyPageItemReviewedNothingBinding
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
