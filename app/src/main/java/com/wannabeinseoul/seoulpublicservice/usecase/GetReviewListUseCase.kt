package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.firestore.ServiceRepository
import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserBanRepository
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository
import com.wannabeinseoul.seoulpublicservice.ui.dialog.review.ReviewItem
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope

class GetReviewListUseCase(
    private val idPrefRepository: IdPrefRepository,
    private val serviceRepository: ServiceRepository,
    private val userBanRepository: UserBanRepository,
) {
    suspend operator fun invoke(svcId: String): List<ReviewItem> =
        coroutineScope {
            val data = async { serviceRepository.getServiceReviews(svcId) }
            val banList = async {
                userBanRepository.getBanList().toMutableList().apply {
                    remove(idPrefRepository.load())
                }
            }

            return@coroutineScope data.await().filter { it.userId !in banList.await() }.sortedByDescending { it.uploadTime }
        }
}
