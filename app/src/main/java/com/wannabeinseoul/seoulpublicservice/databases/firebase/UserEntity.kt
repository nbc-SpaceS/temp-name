package com.wannabeinseoul.seoulpublicservice.databases.firebase

import retrofit2.http.Url

data class UserEntity(
    val userId: String? = "",
    val userName: String? = "",
    val userImage: String? = "",
    val reviewServiceId: List<String>? = emptyList()
)