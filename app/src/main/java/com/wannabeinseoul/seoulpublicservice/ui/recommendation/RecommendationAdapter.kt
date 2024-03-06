package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.wannabeinseoul.seoulpublicservice.databinding.MyPageItemRecommendedBinding
import com.wannabeinseoul.seoulpublicservice.databinding.RecommendationItemBinding

class RecommendationAdapter(private val items: List<RecommendMultiView>) :
    RecyclerView.Adapter<RecommendationAdapter.RecommendationViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecommendationItemBinding.inflate(inflater, parent, false)
        return RecommendationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendationViewHolder, position: Int) {
        val item = items[position]
        when (holder.itemViewType) {
            RecommendMultiView.RecommendType.NEXTWEEK.ordinal -> {
                val viewHolder = holder as RecommendationViewHolder
                viewHolder.bind(item)
            }

            RecommendMultiView.RecommendType.DISABLED.ordinal -> {
                val viewHolder = holder as RecommendationViewHolder
                viewHolder.bind(item)
            }

            RecommendMultiView.RecommendType.TEENAGER.ordinal -> {
                val viewHolder = holder as RecommendationViewHolder
                viewHolder.bind(item)
            }

            RecommendMultiView.RecommendType.AREA.ordinal -> {
                val viewHolder = holder as RecommendationViewHolder
                viewHolder.bind(item)
            }
        }
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun getItemViewType(position: Int): Int {
        return items[position].viewType.ordinal
    }

    inner class RecommendationViewHolder(
        private val binding: RecommendationItemBinding,
        ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: RecommendMultiView) {
            when (item) {
                is RecommendMultiView.NextWeekRecommendation -> {
                    val recommendation = item.data
                    binding.tvSharedTitle.text = "Next Week Recommendation"
                    setupRecommendationView(recommendation, binding)
                }

                is RecommendMultiView.DisabledRecommendation -> {
                    val recommendation = item.data
                    binding.tvSharedTitle.text = "Disabled Recommendation"
                    setupRecommendationView(recommendation, binding)
                }

                is RecommendMultiView.TeenagerRecommendation -> {
                    val recommendation = item.data
                    binding.tvSharedTitle.text = "Teenager Recommendation"
                    setupRecommendationView(recommendation, binding)
                }

                is RecommendMultiView.AreaRecommendation -> {
                    val recommendation = item.data
                    binding.tvSharedTitle.text = "Area Recommendation"
                    setupRecommendationView(recommendation, binding)
                }
            }
        }


        private fun setupRecommendationView(
            recommendation: SealedMulti.Recommendation,
            binding: RecommendationItemBinding
        ) {
            binding.ivRcSmallImage.load(recommendation.imageUrl)
            binding.tvRcIsReservationAvailable.text = recommendation.isReservationAvailable
            binding.tvRcPlaceName.text = recommendation.placeName
            binding.tvRcPayType.text = recommendation.payType
            binding.tvRcAreaName.text = recommendation.areaName
//        binding.tvRcReview.text = "후기 ${recommendation.reviewCount}개"
            //추가 할 예정.
        }
    }
}


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