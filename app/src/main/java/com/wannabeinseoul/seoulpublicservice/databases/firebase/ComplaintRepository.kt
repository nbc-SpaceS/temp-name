package com.wannabeinseoul.seoulpublicservice.databases.firebase

import kotlinx.coroutines.tasks.await

interface ComplaintRepository {
    suspend fun addComplaint(content: ComplaintEntity): String
}

class ComplaintRepositoryImpl : ComplaintRepository {
    override suspend fun addComplaint(content: ComplaintEntity): String {
        if (!checkComplaintId(content.complaintId.toString())) {
            FBRef.complaintRef.child(content.complaintId.toString())
                .child(content.complaintTime.toString()).setValue(content)

            decideAddBanList(content.complaintId.toString())

            return "신고했습니다."
        }

        val complaintSnapshot =
            FBRef.complaintRef.child(content.complaintId.toString()).get().await()

        if (complaintSnapshot.childrenCount >= 3) {
            FBRef.userBanRef.child(content.complaintId.toString()).setValue(content.complaintId)
        }

        for (snapshot in complaintSnapshot.children) {
            val complaint = snapshot.getValue(ComplaintEntity::class.java)
            if (complaint?.id == content.id) {
                return "이미 신고한 사용자입니다."
            }
        }

        return ""
    }

    private suspend fun checkComplaintId(complaintId: String): Boolean {
        val serviceSnapshot = FBRef.complaintRef.get().await()

        for (snapshot in serviceSnapshot.children) {
            if (snapshot.key == complaintId) {
                return true
            }
        }
        return false
    }

    private suspend fun decideAddBanList(complaintId: String) {
        val complaintSnapshot =
            FBRef.complaintRef.child(complaintId).get().await()

        if (complaintSnapshot.childrenCount >= 3) {
            FBRef.userBanRef.child(complaintId).setValue(complaintId)
        }
    }
}