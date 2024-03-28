package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.entity.ReviewEntity
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserRepository
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class UploadReviewUseCase(
    private val idPrefRepository: IdPrefRepository,
    private val reviewRepository: ReviewRepository,
    private val userRepository: UserRepository,
    private val serviceRepository: ServiceRepository,
) {
    suspend operator fun invoke(svcId: String, review: String) {
        val id = idPrefRepository.load()
        val reviewId = id.takeLast(4) + System.currentTimeMillis().toString()

        reviewRepository.addReview(
            reviewId,
            ReviewEntity(
                reviewId = reviewId,
                userId = id,
                svcId = svcId,
                uploadTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                content = review
            )
        )
        userRepository.addUserReview(id, reviewId)
        serviceRepository.addServiceReview(svcId, reviewId)
    }
}
