package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.entity.ReviewEntity
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserRepository
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

        userRepository.addUserReview(id, reviewId)
        serviceRepository.addServiceReview(svcId, reviewId)
        reviewRepository.addReview(
            reviewId,
            ReviewEntity(
                userId = id,
                svcId = svcId,
                uploadTime = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                content = review
            )
        )
    }
}
