package com.wannabeinseoul.seoulpublicservice.usecase

import com.wannabeinseoul.seoulpublicservice.databases.firebase.ComplaintEntity
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ComplaintRepository
import com.wannabeinseoul.seoulpublicservice.databases.firebase.ReviewRepository
import com.wannabeinseoul.seoulpublicservice.dialog.complaint.ComplaintUserInfo
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class ComplaintUserUseCase(
    private val reviewRepository: ReviewRepository,
    private val complaintRepository: ComplaintRepository
) {
    suspend operator fun invoke(userInfo: ComplaintUserInfo): String {
        val reviewId = reviewRepository.getReviewId(userInfo.svcId, userInfo.complaintId)

        return complaintRepository.addComplaint(
            ComplaintEntity(
                userInfo.complaintId,
                LocalDateTime.now()
                    .format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")),
                reviewId,
                userInfo.userId,
                userInfo.svcId
            )
        )
    }
}
