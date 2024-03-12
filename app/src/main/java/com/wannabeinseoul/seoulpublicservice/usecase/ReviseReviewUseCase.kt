package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.firebase.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository

class ReviseReviewUseCase(
    private val idPrefRepository: IdPrefRepository,
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(svcId: String, review: String) {
        val id = idPrefRepository.load()

        reviewRepository.reviseReview(id, svcId, review)
    }
}
