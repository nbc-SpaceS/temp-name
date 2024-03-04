package com.wannabeinseoul.seoulpublicservice.databases.firebase

data class UserEntity(
    val userId: String? = "",
    val userName: String? = "",
    val userImage: String? = "",
    val reviewServiceId: List<String>? = emptyList()
)