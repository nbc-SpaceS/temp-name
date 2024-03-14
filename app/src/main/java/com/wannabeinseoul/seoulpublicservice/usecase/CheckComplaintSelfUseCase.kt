package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.firestore.UserRepository
import com.wannabeinseoul.seoulpublicservice.ui.dialog.complaint.ComplaintUserInfo
import com.wannabeinseoul.seoulpublicservice.pref.IdPrefRepository

class CheckComplaintSelfUseCase(
    private val idPrefRepository: IdPrefRepository,
    private val userRepository: UserRepository,
) {
    suspend operator fun invoke(id: String): Pair<Boolean, ComplaintUserInfo> {
        val complaintName = userRepository.getUser(id)?.userName
        val userId = idPrefRepository.load()

        return if (id == userId) {
            Pair(
                true, ComplaintUserInfo(
                    "",
                    id,
                    complaintName ?: "",
                    userId
                )
            )
        } else {
            Pair(
                false, ComplaintUserInfo(
                    "",
                    id,
                    complaintName ?: "",
                    userId
                )
            )
        }
    }
}
