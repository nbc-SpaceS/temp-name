package com.wannabeinseoul.seoulpublicservice.databases.entity

data class UserEntity(
    val userId: String? = "",
    val userName: String? = "",
    val userProfileImage: String? = "",
    val userColor: String? = "",
    val reviewIdList: List<String>? = emptyList()
)