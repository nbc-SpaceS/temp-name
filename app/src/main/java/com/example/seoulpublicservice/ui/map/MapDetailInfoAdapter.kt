package com.example.seoulpublicservice.ui.map

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.seoulpublicservice.databinding.ItemMapInfoWindowBinding
import com.example.seoulpublicservice.seoul.Row

class MapDetailInfoAdapter(
    private val moveReservationPage: (String) -> Unit,
    private val shareUrl: (String) -> Unit,
    private val moveDetailPage: (String) -> Unit
) : ListAdapter<Row, MapDetailInfoAdapter.InfoViewHolder>(object : DiffUtil.ItemCallback<Row>() {
    override fun areItemsTheSame(oldItem: Row, newItem: Row): Boolean {
        return oldItem.svcid == newItem.svcid
    }

    override fun areContentsTheSame(oldItem: Row, newItem: Row): Boolean {
        return oldItem == newItem
    }

}) {
    abstract class InfoViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        abstract fun onBind(item: Row)
    }

    override fun getItemViewType(position: Int): Int {
        val info = getItem(position)
        return if (info is Row) {
            0
        } else {
            1
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InfoViewHolder {
        return when (viewType) {
            0 -> {
                DetailInfoViewHolder(
                    binding = ItemMapInfoWindowBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    ),
                    moveReservationPage = moveReservationPage,
                    shareUrl = shareUrl,
                    moveDetailPage = moveDetailPage
                )
            }

            else -> {
                UnknownInfoViewHolder(
                    binding = ItemMapInfoWindowBinding.inflate(
                        LayoutInflater.from(parent.context),
                        parent,
                        false
                    )
                )
            }
        }
    }

    override fun onBindViewHolder(holder: InfoViewHolder, position: Int) {
        holder.onBind(getItem(position))
    }

    class DetailInfoViewHolder(
        private val binding: ItemMapInfoWindowBinding,
        private val moveReservationPage: (String) -> Unit,
        private val shareUrl: (String) -> Unit,
        private val moveDetailPage: (String) -> Unit
    ) : InfoViewHolder(binding.root) {
        override fun onBind(item: Row) = with(binding) {
            ivMapInfoPicture.load(item.imgurl)
            tvMapInfoRegion.text = item.areanm
            tvMapInfoService.text =
                HtmlCompat.fromHtml(item.svcnm, HtmlCompat.FROM_HTML_MODE_LEGACY)
            tvMapInfoPay.text = item.payatnm
            tvMapInfoAvailablility.text = item.svcstatnm
            btnMapInfoReservation.text = when (item.svcstatnm) {
                "안내중" -> {
                    "예약안내"
                }

                "접수중" -> {
                    "예약하기"
                }

                else -> {
                    item.svcstatnm
                }
            }

            binding.btnMapInfoReservation.isEnabled = when (item.svcstatnm) {
                "안내중", "접수중" -> {
                    true
                }

                else -> {
                    false
                }
            }

            binding.btnMapInfoReservation.setOnClickListener {
                moveReservationPage(item.svcurl)
            }

            binding.ivMapInfoShareBtn.setOnClickListener {
                shareUrl(item.svcurl)
            }

            binding.clMapInfoWindow.setOnClickListener {
                moveDetailPage(item.svcid)
            }
        }
    }

    class UnknownInfoViewHolder(
        private val binding: ItemMapInfoWindowBinding
    ) : InfoViewHolder(binding.root) {
        override fun onBind(item: Row) = Unit
    }
}