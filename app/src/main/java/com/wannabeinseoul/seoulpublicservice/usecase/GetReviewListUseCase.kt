package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.firebase.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.UserBanRepository
import com.wannabeinseoul.seoulpublicservice.dialog.review.ReviewItem
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository

class GetReviewListUseCase(
    private val idPrefRepository: IdPrefRepository,
    private val serviceRepository: ServiceRepository,
    private val userBanRepository: UserBanRepository,
) {
    suspend operator fun invoke(svcId: String): List<ReviewItem> {
        val data = serviceRepository.getServiceReviews(svcId)
        val banList = userBanRepository.getBanList().toMutableList().apply {
            remove(idPrefRepository.load())
        }

        return data.filter { it.userId !in banList }
    }
}
