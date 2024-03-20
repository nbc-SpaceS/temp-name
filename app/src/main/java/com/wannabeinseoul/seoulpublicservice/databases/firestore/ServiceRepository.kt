package com.wannabeinseoul.seoulpublicservice.databases.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wannabeinseoul.seoulpublicservice.databases.entity.ReviewEntity
import com.wannabeinseoul.seoulpublicservice.databases.entity.ServiceEntity
import com.wannabeinseoul.seoulpublicservice.databases.entity.UserEntity
import com.wannabeinseoul.seoulpublicservice.ui.dialog.review.ReviewItem
import kotlinx.coroutines.tasks.await

interface ServiceRepository {

    suspend fun addServiceReview(
        svcId: String,
        reviewId: String
    )

    suspend fun getService(
        svcId: String
    ): ServiceEntity?

    suspend fun getServiceReviews(
        svcId: String
    ): List<ReviewItem>

    suspend fun getServiceReviewsCount(
        svcId: String
    ): Int

    suspend fun getServiceReviewsCount(
        svcIdList: List<String>
    ): List<Int>
}

class ServiceRepositoryImpl : ServiceRepository {

    private val fireStore = Firebase.firestore

    override suspend fun addServiceReview(svcId: String, reviewId: String) {
        val service = getService(svcId)
        fireStore.collection("service").document(svcId)
            .update("reviewIdList", service?.reviewIdList.orEmpty().toMutableList() + reviewId)
    }

    override suspend fun getService(svcId: String): ServiceEntity? {
        if (!checkService(svcId)) fireStore.collection("service").document(svcId)
            .set(ServiceEntity(svcId))

        return fireStore.collection("service").document(svcId).get().await()
            .toObject(ServiceEntity::class.java)
    }

    override suspend fun getServiceReviews(svcId: String): List<ReviewItem> {
        getService(svcId)

        val serviceReviewList =
            fireStore.collection("review").whereEqualTo("svcId", svcId).get().await()
                .toObjects(ReviewEntity::class.java)
        val resultList = mutableListOf<ReviewItem>()

        if (serviceReviewList.isNotEmpty()) {
            val serviceReviewUserList = HashMap<String, UserEntity>()
            fireStore.collection("user").whereIn("userId", serviceReviewList.map { it.userId })
                .get().await().toObjects(UserEntity::class.java).forEach { user ->
                    serviceReviewUserList[user.userId ?: ""] = user
                }

            serviceReviewList.forEach {
                resultList.add(
                    ReviewItem(
                        it.reviewId ?: "",
                        it.userId ?: "",
                        serviceReviewUserList[it.userId]?.userName ?: "",
                        it.uploadTime ?: "",
                        it.content ?: "",
                        serviceReviewUserList[it.userId]?.userColor ?: "",
                        serviceReviewUserList[it.userId]?.userProfileImage ?: ""
                    )
                )
            }
        }

        return resultList
    }

    override suspend fun getServiceReviewsCount(svcId: String): Int {
        val service = getService(svcId)

        return service?.reviewIdList?.size ?: 0
    }

    override suspend fun getServiceReviewsCount(svcIdList: List<String>): List<Int> {
        if (svcIdList.isEmpty()) return emptyList()

        svcIdList.forEach { svcId ->
            if (!checkService(svcId)) fireStore.collection("service").document(svcId)
                .set(ServiceEntity(svcId))
        }

        val count = fireStore.collection("service").whereIn("svcId", svcIdList).get().await()
            .toObjects(ServiceEntity::class.java)

        return svcIdList.map { svcId -> count.find { it.svcId == svcId }?.reviewIdList?.size ?: 0 }
    }


    private suspend fun checkService(svcId: String): Boolean =
        fireStore.collection("service").document(svcId).get().await().exists()
}