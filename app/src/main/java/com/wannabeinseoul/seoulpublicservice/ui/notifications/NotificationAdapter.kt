package com.wannabeinseoul.seoulpublicservice.ui.notifications

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.wannabeinseoul.seoulpublicservice.R
import com.wannabeinseoul.seoulpublicservice.databinding.ItemNotificationInfoWindowBinding

class NotificationAdapter(
    private val moveDetail: (String) -> Unit
) : ListAdapter<NotificationInfo, NotificationAdapter.NotificationViewHolder>(object :
    DiffUtil.ItemCallback<NotificationInfo>() {
    override fun areItemsTheSame(oldItem: NotificationInfo, newItem: NotificationInfo): Boolean {
        return oldItem.svcId == newItem.svcId
    }

    override fun areContentsTheSame(oldItem: NotificationInfo, newItem: NotificationInfo): Boolean {
        return oldItem == newItem
    }
}) {

    class NotificationViewHolder(
        private val binding: ItemNotificationInfoWindowBinding,
        private val moveDetail: (String) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: NotificationInfo) = with(binding) {
            tvNotificationItemTitle.text = item.comment
            tvNotificationItemTarget.text = item.svcName
            when (item.comment) {
                "예약시작 하루전 알림" -> ivNotificationItemIcon.setImageResource(R.drawable.ic_reservation_start)
                "예약마감 하루전 알림" -> ivNotificationItemIcon.setImageResource(R.drawable.ic_reservation_end)
                "예약가능 알림" -> ivNotificationItemIcon.setImageResource(R.drawable.ic_notification)
            }

            clNotificationItem.setOnClickListener {
                moveDetail(item.svcId)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotificationViewHolder {
        return NotificationViewHolder(
            ItemNotificationInfoWindowBinding.inflate(
                LayoutInflater.from(
                    parent.context
                ), parent, false
            ),
            moveDetail = moveDetail
        )
    }

    override fun onBindViewHolder(holder: NotificationViewHolder, position: Int) {
        holder.bind(getItem(position))
    }
}