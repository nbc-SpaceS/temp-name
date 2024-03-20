package com.wannabeinseoul.seoulpublicservice.databases.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wannabeinseoul.seoulpublicservice.databases.entity.ReviewEntity
import kotlinx.coroutines.tasks.await

interface ReviewRepository {
    fun addReview(reviewId: String, reviewInfo: ReviewEntity)

    suspend fun reviseReview(id: String, svcId: String, review: String)

    suspend fun deleteReview(reviewId: String)

    suspend fun getReviewId(svcId: String, userId: String): String

    suspend fun checkCredentials(id: String, svcId: String): Boolean
}

class ReviewRepositoryImpl : ReviewRepository {

    private val fireStore = Firebase.firestore

    override fun addReview(reviewId: String, reviewInfo: ReviewEntity) {
        fireStore.collection("review").document(reviewId).set(reviewInfo)
    }

    override suspend fun reviseReview(id: String, svcId: String, review: String) {
        val targetReview =
            fireStore.collection("review").whereEqualTo("userId", id).whereEqualTo("svcId", svcId)
                .get().await().toObjects(ReviewEntity::class.java)

        fireStore.collection("review").document(targetReview[0].reviewId ?: "")
            .update("content", review)
    }

    override suspend fun deleteReview(reviewId: String) {
        fireStore.collection("review").document(reviewId).delete().await()
    }

    override suspend fun getReviewId(svcId: String, userId: String): String =
        fireStore.collection("review").whereEqualTo("userId", userId).whereEqualTo("svcId", svcId)
            .get().await().toObjects(ReviewEntity::class.java)[0].reviewId ?: ""

    override suspend fun checkCredentials(id: String, svcId: String): Boolean {
        return fireStore.collection("review").whereEqualTo("userId", id)
            .whereEqualTo("svcId", svcId)
            .get().await().isEmpty
    }
}