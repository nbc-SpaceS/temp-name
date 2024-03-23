package com.wannabeinseoul.seoulpublicservice.ui.mypage

import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity

data class ReviewedData(
    val row: ReservationEntity,
    val content: String,
    val uploadTime: String,
)
