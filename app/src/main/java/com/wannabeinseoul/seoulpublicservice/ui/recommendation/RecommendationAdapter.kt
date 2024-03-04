package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity
import com.wannabeinseoul.seoulpublicservice.databinding.CategoryItemBinding

//class RecommendationAdapter(private val recyclerView: RecyclerView) :
//    RecyclerView.Adapter<RecommendationAdapter.CommonViewHolder>() {
//
//    private var items: List<RecommendMultiView> = listOf()
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CommonViewHolder {
//        val inflater = LayoutInflater.from(parent.context)
//        return when (viewType) {
//            RecommendMultiView.RecommendType.NEXTWEEK.ordinal -> NextWeekRecommendationHolder(
//                MyPageItemNextWeekBinding.inflate(inflater, parent, false)
//            )
//            RecommendMultiView.RecommendType.DISABLED.ordinal -> DisabledRecommendationHolder(
//                MyPageItemDisabledBinding.inflate(inflater, parent, false)
//            )
//            RecommendMultiView.RecommendType.TEENAGER.ordinal -> TeenagerRecommendationHolder(
//                MyPageItemTeenagerBinding.inflate(inflater, parent, false)
//            )
//            RecommendMultiView.RecommendType.AREA.ordinal -> AreaRecommendationHolder(
//                MyPageItemAreaBinding.inflate(inflater, parent, false)
//            )
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
//    init {
//        recyclerView.apply {
//            adapter = this@RecommendationAdapter
//            layoutManager = LinearLayoutManager(context)
//        }
//    }
//
//    abstract inner class CommonViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
//        abstract fun bind(item: RecommendMultiView)
//    }
//
//    inner class NextWeekRecommendationHolder(private val binding: MyPageItemNextWeekBinding) :
//        CommonViewHolder(binding.root) {
//
//        override fun bind(item: RecommendMultiView) {
//            val data = (item as RecommendMultiView.NextWeekRecommendation).data
//            // Bind data to the views
//        }
//    }
//
//    inner class DisabledRecommendationHolder(private val binding: MyPageItemDisabledBinding) :
//        CommonViewHolder(binding.root) {
//
//        override fun bind(item: RecommendMultiView) {
//            val data = (item as RecommendMultiView.DisabledRecommendation).data
//            // Bind data to the views
//        }
//    }
//
//    inner class TeenagerRecommendationHolder(private val binding: MyPageItemTeenagerBinding) :
//        CommonViewHolder(binding.root) {
//
//        override fun bind(item: RecommendMultiView) {
//            val data = (item as RecommendMultiView.TeenagerRecommendation).data
//            // Bind data to the views
//        }
//    }
//
//    inner class AreaRecommendationHolder(private val binding: MyPageItemAreaBinding) :
//        CommonViewHolder(binding.root) {
//
//        override fun bind(item: RecommendMultiView) {
//            val data = (item as RecommendMultiView.AreaRecommendation).data
//            // Bind data to the views
//        }
//    }
//}
//테스트중







class RecommendationAdapter : RecyclerView.Adapter<RecommendationAdapter.ViewHolder>() {





    private var items: List<ReservationEntity> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = CategoryItemBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = items[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return items.size
    }



        fun setItems(newItems: List<SealedMulti>) {
        val mappedItems =
            newItems.filterIsInstance<SealedMulti.Recommendation>().map { recommendation ->
                ReservationEntity(
                    GUBUN = recommendation.serviceList,
                    PLACENM = recommendation.placeName,
                    PAYATNM = recommendation.payType,
                    SVCSTATNM = recommendation.isReservationAvailable,
                    AREANM = recommendation.areaName,
                    IMGURL = recommendation.imageUrl,
                    MAXCLASSNM = "",
                    MINCLASSNM = "",
                    RCPTBGNDT = "",
                    RCPTENDDT = "",
                    REVSTDDAY = "",
                    REVSTDDAYNM = "",
                    SVCID = "",
                    SVCNM = "",
                    SVCOPNBGNDT = "",
                    SVCOPNENDDT = "",
                    SVCURL = "",
                    TELNO = "",
                    USETGTINFO = "",
                    V_MAX = "",
                    V_MIN = "",
                    X = "",
                    Y = "",
                    DTLCONT = ""
                )
            }
        items = mappedItems
        notifyDataSetChanged()
        Log.d("RecommendationAdapter", "Item count: ${items.size}")
    }

    inner class ViewHolder(private val binding: CategoryItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: ReservationEntity) {
            binding.tvPlaceName.text = item.PLACENM
            binding.tvIsReservationAvailable.text = item.SVCSTATNM
            binding.tvPayType.text = item.PAYATNM
            binding.tvAreaName.text = item.AREANM

            Glide.with(binding.ivSmallImage.context)
                .load(item.IMGURL) // 예약 서비스의 이미지 URL
                .into(binding.ivSmallImage)
//            loadImageWithCoil(item.IMGURL, binding.ivSmallImage)
            //수정 예정
        }
    }
}