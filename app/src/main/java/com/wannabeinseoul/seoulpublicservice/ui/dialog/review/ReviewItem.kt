package com.wannabeinseoul.seoulpublicservice.ui.dialog.review

data class ReviewItem(
    val userId: String,
    val userName: String,
    val uploadTime: String,
    val content: String,
    val userColor: String,
    val userProfileImage: String
)