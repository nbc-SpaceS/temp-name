package com.wannabeinseoul.seoulpublicservice.databases.entity

data class ReviewEntity(
    val reviewId: String? = "",
    val svcId: String? = "",
    val userId: String? = "",
    val uploadTime: String? = "",
    val content: String? = ""
)