package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.firestore.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserRepository
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository

class DeleteReviewUseCase(
    private val reviewRepository: ReviewRepository,
    private val serviceRepository: ServiceRepository,
    private val userRepository: UserRepository,
    private val idPrefRepository: IdPrefRepository
) {
    suspend operator fun invoke(svcId: String, reviewId: String) {
        reviewRepository.deleteReview(reviewId)
        serviceRepository.deleteServiceReview(svcId, reviewId)
        userRepository.deleteUserReview(idPrefRepository.load(), reviewId)
    }
}
