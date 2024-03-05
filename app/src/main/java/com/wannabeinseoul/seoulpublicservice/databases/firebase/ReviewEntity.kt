package com.wannabeinseoul.seoulpublicservice.databases.firebase

data class ReviewEntity(
    val svcId: String? = "",
    val userId: String? = "",
    val uploadTime: String? = "",
    val content: String? = ""
)