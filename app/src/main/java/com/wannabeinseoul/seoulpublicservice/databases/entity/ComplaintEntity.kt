package com.wannabeinseoul.seoulpublicservice.databases.entity

data class ComplaintEntity(
    val complaintId: String? = "",      // 신고당한 사람
    val complaintTime: String? = "",
    val reviewId: String? = "",
    val id: String? = "",               // 신고한 사람
    val svcId: String? = ""
)