package com.wannabeinseoul.seoulpublicservice.databases.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wannabeinseoul.seoulpublicservice.databases.entity.ComplaintEntity
import kotlinx.coroutines.tasks.await

interface ComplaintRepository {
    suspend fun addComplaint(content: ComplaintEntity): String
}

class ComplaintRepositoryImpl : ComplaintRepository {

    private val fireStore = Firebase.firestore

    override suspend fun addComplaint(content: ComplaintEntity): String {
        if (!checkComplaintId(content.complaintId.toString())) {
            fireStore.collection("complaint").document(content.complaintId.toString())
                .collection("detailInfo").document(content.complaintTime.toString()).set(content)

            decideAddBanList(content.complaintId.toString())

            return "신고했습니다."
        }

        if (fireStore.collection("complaint").document(content.complaintId.toString())
                .collection("detailInfo").whereEqualTo("id", content.id).get().await()
                .toObjects(ComplaintEntity::class.java).isNotEmpty()
        ) return "이미 신고한 사용자입니다."

        return ""
    }

    private suspend fun checkComplaintId(complaintId: String): Boolean =
        fireStore.collection("complaint").document(complaintId).get().await().exists()

    private suspend fun decideAddBanList(complaintId: String) {
        val targetUser =
            fireStore.collection("complaint").document(complaintId).get().await().data?.size ?: 0

        if (targetUser >= 10) {
            fireStore.collection("userBan").document(complaintId).set(complaintId)
        }
    }
}