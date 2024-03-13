package com.wannabeinseoul.seoulpublicservice.databases.entity

data class ServiceEntity(
    val svcId: String? = "",
    val reviewIdList: List<String>? = emptyList()
)
