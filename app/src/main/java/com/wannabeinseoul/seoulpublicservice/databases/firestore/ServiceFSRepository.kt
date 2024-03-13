package com.wannabeinseoul.seoulpublicservice.databases.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wannabeinseoul.seoulpublicservice.databases.entity.ReviewEntity
import com.wannabeinseoul.seoulpublicservice.databases.entity.ServiceEntity
import com.wannabeinseoul.seoulpublicservice.databases.entity.UserEntity
import com.wannabeinseoul.seoulpublicservice.databases.firebase.FBRef
import com.wannabeinseoul.seoulpublicservice.ui.dialog.review.ReviewItem
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.tasks.await

interface ServiceFSRepository {

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

    suspend fun getServiceReviewsCount(
        svcIdList: List<String>
    ) : List<Int>
}

class ServiceFSRepositoryImpl: ServiceFSRepository {

    private val fireStore = Firebase.firestore

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
                    resultList.add(
                        ReviewItem(
                        review.userId ?: "",
                        user?.userName ?: "",
                        review.uploadTime ?: "",
                        review.content ?: "",
                        user?.userColor ?: "",
                        user?.userProfileImage ?: ""
                    )
                    )
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

    override suspend fun getServiceReviewsCount(svcIdList: List<String>) =
        coroutineScope {
            svcIdList.map { svcId ->
                async {
                    getServiceReviewsCount(svcId)
                }
            }.awaitAll()
        }

    private suspend fun checkService(svcId: String): Boolean {
        fireStore.collection("service").document(svcId).get().addOnSuccessListener {

        }
        val serviceSnapshot = FBRef.serviceRef.get().await()

        for (snapshot in serviceSnapshot.children) {
            if (snapshot.key == svcId) {
                return true
            }
        }
        return false
    }
}