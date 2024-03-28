package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.firestore.ReviewRepository

class ReviseReviewUseCase(
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(reviewId: String, review: String) {
        reviewRepository.reviseReview(reviewId, review)
    }
}
