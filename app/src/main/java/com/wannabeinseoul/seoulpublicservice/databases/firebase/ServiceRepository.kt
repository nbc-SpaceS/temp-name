package com.wannabeinseoul.seoulpublicservice.databases.firebase

import android.util.Log
import com.wannabeinseoul.seoulpublicservice.dialog.review.ReviewItem
import kotlinx.coroutines.tasks.await

interface ServiceRepository {

    suspend fun addServiceReview(
        svcId: String,
        reviewId: String
    )

    suspend fun getService(
        svcId: String
    ) : ServiceEntity?

    suspend fun getServiceReviews(
        svcId: String
    ) : List<ReviewItem>

    suspend fun getServiceReviewsCount(
        svcId: String
    ) : Int
}

class ServiceRepositoryImpl: ServiceRepository {

    override suspend fun addServiceReview(svcId: String, reviewId: String) {
        val service = getService(svcId)

        service?.copy(
            reviewIdList = service.reviewIdList.orEmpty().toMutableList() + reviewId
        )?.let { FBRef.serviceRef.child(svcId).setValue(it) }
    }

    override suspend fun getService(svcId: String): ServiceEntity? {
        if (!checkService(svcId)) {
            FBRef.serviceRef.child(svcId).setValue(ServiceEntity(svcId))
        }

        val serviceSnapshot = FBRef.serviceRef.get().await()

        var targetService: ServiceEntity? = null
        for (snapshot in serviceSnapshot.children) {
            if (snapshot.key == svcId) {
                targetService = snapshot.getValue(ServiceEntity::class.java)
                break
            }
        }

        return targetService
    }

    override suspend fun getServiceReviews(svcId: String): List<ReviewItem> {
        val service = getService(svcId)

        val reviewsSnapshot = FBRef.reviewRef.get().await()
        val userSnapShot = FBRef.userRef.get().await()

        val targetReviewList: MutableList<ReviewEntity> = mutableListOf()
        service?.reviewIdList?.forEach { reviewId ->
            for (snapshot in reviewsSnapshot.children) {
                if (snapshot.key == reviewId) {
                    val review = snapshot.getValue(ReviewEntity::class.java)
                    if (review != null) targetReviewList.add(review)
                    break
                }
            }
        }

        val resultList: MutableList<ReviewItem> = mutableListOf()
        targetReviewList.forEach { review ->
            for (snapshot in userSnapShot.children) {
                if (snapshot.key == review.userId) {
                    val user = snapshot.getValue(UserEntity::class.java)
                    resultList.add(ReviewItem(
                        review.userId ?: "",
                        user?.userName ?: "",
                        review.uploadTime ?: "",
                        review.content ?: "",
                        user?.userColor ?: "",
                        user?.userProfileImage ?: ""
                    ))
                    break
                }
            }
        }

        return resultList
    }

    override suspend fun getServiceReviewsCount(svcId: String): Int {
        val service = getService(svcId)

        return service?.reviewIdList?.size ?: 0
    }

    private suspend fun checkService(svcId: String): Boolean {
        val serviceSnapshot = FBRef.serviceRef.get().await()

        for (snapshot in serviceSnapshot.children) {
            if (snapshot.key == svcId) {
                return true
            }
        }
        return false
    }
}