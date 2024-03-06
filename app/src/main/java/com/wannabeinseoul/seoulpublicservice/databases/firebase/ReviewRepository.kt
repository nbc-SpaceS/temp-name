package com.wannabeinseoul.seoulpublicservice.databases.firebase

import kotlinx.coroutines.tasks.await

interface ReviewRepository {
    fun addReview(reviewId: String, reviewInfo: ReviewEntity)

    suspend fun reviseReview(id: String, svcId: String, review: String)

    suspend fun checkCredentials(id: String, svcId: String): Boolean
}

class ReviewRepositoryImpl : ReviewRepository {

    override fun addReview(reviewId: String, reviewInfo: ReviewEntity) {
        FBRef.reviewRef.child(reviewId).setValue(reviewInfo)
    }

    override suspend fun reviseReview(id: String, svcId: String, review: String) {
        val reviewSnapshot = FBRef.reviewRef.get().await()

        for (snapshot in reviewSnapshot.children) {
            val currentReview = snapshot.getValue(ReviewEntity::class.java)
            if (currentReview?.userId == id && currentReview.svcId == svcId) {
                FBRef.reviewRef.child(snapshot.key!!).setValue(currentReview.copy(content = review))
                break
            }
        }
    }

    override suspend fun checkCredentials(id: String, svcId: String): Boolean {
        val reviewSnapshot = FBRef.reviewRef.get().await()

        for (snapshot in reviewSnapshot.children) {
            val review = snapshot.getValue(ReviewEntity::class.java)
            if (review?.userId == id && review.svcId == svcId) {
                return false
            }
        }

        return true
    }
}