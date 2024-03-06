package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemRecommendedBinding
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemRecommendedTipBinding

class RecommendationAdapter(private var items: List<MultiView>) :
    RecyclerView.Adapter<RecommendationAdapter.CommonViewHolder>() {

    sealed interface MultiView {

//        enum class RecommendType {
//            NEXTWEEK, DISABLED, TEENAGER, AREA,
////            LOADING,
//        }
//
//        val viewType: RecommendType

        enum class Type {
            HORIZONTAL,
            TIP,
        }

        val viewType: Type

        data class Horizontal(
            val headerTitle: String,
            val adapter: RecommendationHorizontalAdapter,
        ) : MultiView {
            override val viewType: Type = Type.HORIZONTAL
        }

        data class Tip(
            val title: String,
            val content: String,
        ) : MultiView {
            override val viewType: Type = Type.TIP
        }


//        data class NextWeekRecommendation(val data: SealedMulti.Recommendation) : MultiView {
//            override val viewType = Type.NEXTWEEK
//        }
//
//        data class DisabledRecommendation(val data: SealedMulti.Recommendation) : MultiView {
//            override val viewType = Type.DISABLED
//        }
//
//        data class TeenagerRecommendation(val data: SealedMulti.Recommendation) : MultiView {
//            override val viewType = Type.TEENAGER
//        }
//
//        data class AreaRecommendation(val data: SealedMulti.Recommendation) : MultiView {
//            override val viewType = Type.AREA
//        }
    }

    abstract inner class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        abstract fun onBind(item: MultiView)
    }

    inner class RecommendationViewHolder(
        private val binding: MyPageItemRecommendedBinding,
    ) : CommonViewHolder(binding.root) {

        override fun onBind(item: MultiView) {
            binding.reShared.adapter = (item as MultiView.Horizontal).adapter
            binding.tvSharedText.text = item.headerTitle


//            when (item) {
//                is MultiView.NextWeekRecommendation -> {
//                    val recommendation = item.data
//                    binding.tvSharedTitle.text = "다음주부터 사용가능한 공공서비스"
//                    setupRecommendationView(recommendation, binding)
//                }
//
//                is MultiView.DisabledRecommendation -> {
//                    val recommendation = item.data
//                    binding.tvSharedTitle.text = "장애인들을 대상으로 하는 공공서비스"
//                    setupRecommendationView(recommendation, binding)
//                }
//
//                is MultiView.TeenagerRecommendation -> {
//                    val recommendation = item.data
//                    binding.tvSharedTitle.text = "청소년들을 대상으로 하는 공공서비스"
//                    setupRecommendationView(recommendation, binding)
//                }
//
//                is MultiView.AreaRecommendation -> {
//                    val recommendation = item.data
//                    binding.tvSharedTitle.text = "송파구에서 누릴 수 있는 공공서비스 전체"
//                    setupRecommendationView(recommendation, binding)
//                }
//            }
        }
    }

    inner class TipViewHolder(private val b: MyPageItemRecommendedTipBinding) :
        CommonViewHolder(b.root) {

        override fun onBind(item: MultiView) {
//                b.tvTitle.text = (item as MultiView.Tip).title
            b.tvTip.text = (item as MultiView.Tip).content
        }
    }

    override fun getItemCount(): Int = items.size

    override fun getItemViewType(position: Int): Int = items[position].viewType.ordinal

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
        return when (MultiView.Type.values()[viewType]) {
            MultiView.Type.HORIZONTAL -> RecommendationViewHolder(
                MyPageItemRecommendedBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )

            MultiView.Type.TIP -> TipViewHolder(
                MyPageItemRecommendedTipBinding
                    .inflate(LayoutInflater.from(parent.context), parent, false)
            )
        }
    }

    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
        holder.onBind(items[position])


//        val item = items[position]
//        when (holder.itemViewType) {
//            MultiView.Type.NEXTWEEK.ordinal -> {
//                val viewHolder = holder as RecommendationViewHolder
//                viewHolder.onBind(item)
//            }
//
//            MultiView.Type.DISABLED.ordinal -> {
//                val viewHolder = holder as RecommendationViewHolder
//                viewHolder.onBind(item)
//            }
//
//            MultiView.Type.TEENAGER.ordinal -> {
//                val viewHolder = holder as RecommendationViewHolder
//                viewHolder.onBind(item)
//            }
//
//            MultiView.Type.AREA.ordinal -> {
//                val viewHolder = holder as RecommendationViewHolder
//                viewHolder.onBind(item)
//            }
//        }
    }


    fun submitList(newItems: List<MultiView>) {
        items = newItems
        notifyDataSetChanged()
    }



//        private fun setupRecommendationView(
//            recommendation: SealedMulti.Recommendation,
//            binding: RecommendationItemBinding
//        ) {
//            binding.ivRcSmallImage.load(recommendation.imageUrl)
//            binding.tvRcPlaceName.text = recommendation.placeName
//            binding.tvRcPayType.text = recommendation.payType
//            binding.tvRcAreaName.text = recommendation.areaName
//            binding.tvRcIsReservationAvailable.text = if (recommendation.isReservationAvailable) {
//                "예약가능"
//            } else {
//                "예약종료"
//            }
////        binding.tvRcReview.text = "후기 ${recommendation.reviewCount}개"
//            //추가 할 예정.
//        }

}


////////////////////////// 이 밑은 무덤이야

// class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.CommonViewHolder>() {
//
//    private var items: List<RecommendMultiView> = listOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = CategoryItemBinding.inflate(inflater, parent, false)
//        return when (viewType) {
//            RecommendMultiView.RecommendType.NEXTWEEK.ordinal -> {
//                NextWeekRecommendationHolder(binding)
//            }
//            RecommendMultiView.RecommendType.DISABLED.ordinal -> {
//                DisabledRecommendationHolder(binding)
//            }
//            RecommendMultiView.RecommendType.TEENAGER.ordinal -> {
//                TeenagerRecommendationHolder(binding)
//            }
//            RecommendMultiView.RecommendType.AREA.ordinal -> {
//                AreaRecommendationHolder(binding)
//            }
//            else -> throw IllegalArgumentException("Invalid view type")
//        }
//    }
//
//    override fun onBindViewHolder(holder: CommonViewHolder, position: Int) {
//        val item = items[position]
//        holder.bind(item)
//    }
//
//    override fun getItemCount(): Int = items.size
//
//    override fun getItemViewType(position: Int): Int = items[position].viewType.ordinal
//
//    fun setItems(newItems: List<RecommendMultiView>) {
//        items = newItems
//        notifyDataSetChanged()
//    }
//
//    abstract inner class CommonViewHolder(protected val binding: CategoryItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//        abstract fun bind(item: RecommendMultiView)
//    }
//
//    inner class NextWeekRecommendationHolder(binding: CategoryItemBinding) :
//        CommonViewHolder(binding) {
//        override fun bind(item: RecommendMultiView) {
//            val data = (item as RecommendMultiView.NextWeekRecommendation).data
//            // Bind data to views
//            with(binding) {
//                ivSmallImage.load(data.imageUrl)
//                tvAreaName.text = data.areaName
//                tvIsReservationAvailable.text = data.isReservationAvailable
//                // Bind other data as needed
//            }
//        }
//    }
//
//    inner class DisabledRecommendationHolder(binding: CategoryItemBinding) :
//        CommonViewHolder(binding) {
//        override fun bind(item: RecommendMultiView) {
//            val data = (item as RecommendMultiView.DisabledRecommendation).data
//            // Bind data to views
//            with(binding) {
//                ivSmallImage.load(data.imageUrl)
//                tvAreaName.text = data.areaName
//                tvIsReservationAvailable.text = data.isReservationAvailable
//                // Bind other data as needed
//            }
//        }
//    }
//
//    inner class TeenagerRecommendationHolder(binding: CategoryItemBinding) :
//        CommonViewHolder(binding) {
//        override fun bind(item: RecommendMultiView) {
//            val data = (item as RecommendMultiView.TeenagerRecommendation).data
//            // Bind data to views
//            with(binding) {
//                ivSmallImage.load(data.imageUrl)
//                tvAreaName.text = data.areaName
//                tvIsReservationAvailable.text = data.isReservationAvailable
//                // Bind other data as needed
//            }
//        }
//    }
//
//    inner class AreaRecommendationHolder(binding: CategoryItemBinding) :
//        CommonViewHolder(binding) {
//        override fun bind(item: RecommendMultiView) {
//            val data = (item as RecommendMultiView.AreaRecommendation).data
//            // Bind data to views
//            with(binding) {
//                ivSmallImage.load(data.imageUrl)
//                tvAreaName.text = data.areaName
//                tvIsReservationAvailable.text = data.isReservationAvailable
//                // Bind other data as needed
//            }
//        }
//    }
//}
//테스트중


//class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {
//
//
//
//
//
//    private var items: List<ReservationEntity> = listOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        val binding = CategoryItemBinding.inflate(inflater, parent, false)
//        return ViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        val item = items[position]
//        holder.bind(item)
//    }
//
//    override fun getItemCount(): Int {
//        return items.size
//    }
//
//
//
//        fun setItems(newItems: List<SealedMulti>) {
//        val mappedItems =
//            newItems.filterIsInstance<SealedMulti.Recommendation>().map { recommendation ->
//                ReservationEntity(
//                    GUBUN = recommendation.serviceList,
//                    PLACENM = recommendation.placeName,
//                    PAYATNM = recommendation.payType,
//                    SVCSTATNM = recommendation.isReservationAvailable,
//                    AREANM = recommendation.areaName,
//                    IMGURL = recommendation.imageUrl,
//                    MAXCLASSNM = "",
//                    MINCLASSNM = "",
//                    RCPTBGNDT = "",
//                    RCPTENDDT = "",
//                    REVSTDDAY = "",
//                    REVSTDDAYNM = "",
//                    SVCID = "",
//                    SVCNM = "",
//                    SVCOPNBGNDT = "",
//                    SVCOPNENDDT = "",
//                    SVCURL = "",
//                    TELNO = "",
//                    USETGTINFO = "",
//                    V_MAX = "",
//                    V_MIN = "",
//                    X = "",
//                    Y = "",
//                    DTLCONT = ""
//                )
//            }
//        items = mappedItems
//        notifyDataSetChanged()
//        Log.d("RecommendationAdapter", "Item count: ${items.size}")
//    }
//
//    inner class ViewHolder(private val binding: CategoryItemBinding) :
//        RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(item: ReservationEntity) {
//            binding.tvPlaceName.text = item.PLACENM
//            binding.tvIsReservationAvailable.text = item.SVCSTATNM
//            binding.tvPayType.text = item.PAYATNM
//            binding.tvAreaName.text = item.AREANM
//
//            Glide.with(binding.ivSmallImage.context)
//                .load(item.IMGURL) // 예약 서비스의 이미지 URL
//                .into(binding.ivSmallImage)
////            loadImageWithCoil(item.IMGURL, binding.ivSmallImage)
//            //수정 예정
//        }
//    }
//}