package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.firestore.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository

class CheckCredentialsUseCase(
    private val idPrefRepository: IdPrefRepository,
    private val reviewRepository: ReviewRepository
) {
    suspend operator fun invoke(svcId: String): Boolean {
        val id = idPrefRepository.load()

        return reviewRepository.checkCredentials(id, svcId)
    }
}
