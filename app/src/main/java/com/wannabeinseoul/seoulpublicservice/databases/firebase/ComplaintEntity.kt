package com.wannabeinseoul.seoulpublicservice.databases.firebase

data class ComplaintEntity(
    val complaintId: String? = "",
    val idList: List<String>? = emptyList()
)
