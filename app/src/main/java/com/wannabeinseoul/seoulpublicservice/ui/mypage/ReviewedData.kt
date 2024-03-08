package com.wannabeinseoul.seoulpublicservice.ui.mypage

import com.wannabeinseoul.seoulpublicservice.seoul.Row

data class ReviewedData(
    val row: Row,
    val content: String,
    val uploadTime: String,
)
