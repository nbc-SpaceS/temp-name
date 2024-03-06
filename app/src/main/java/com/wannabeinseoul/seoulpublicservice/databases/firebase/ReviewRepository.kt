package com.wannabeinseoul.seoulpublicservice.databases.firebase

interface ReviewRepository {
    fun addReview(reviewId: String, reviewInfo: ReviewEntity)
}

class ReviewRepositoryImpl: ReviewRepository {

    override fun addReview(reviewId: String, reviewInfo: ReviewEntity) {
        FBRef.reviewRef.child(reviewId).setValue(reviewInfo)
    }
}