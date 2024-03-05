package com.wannabeinseoul.seoulpublicservice.databases.firebase

data class ServiceEntity(
    val svcId: String? = "",
    val reviewIdList: List<String>? = emptyList()
)
