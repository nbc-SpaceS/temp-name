package com.wannabeinseoul.seoulpublicservice.databases.firebase

import kotlinx.coroutines.tasks.await

interface ComplaintRepository {
    suspend fun addComplaint(id: String, complaintId: String): String
}

class ComplaintRepositoryImpl : ComplaintRepository {
    override suspend fun addComplaint(id: String, complaintId: String): String {
        if (!checkComplaintId(complaintId)) {
            FBRef.complaintRef.child(complaintId).setValue(ComplaintEntity(complaintId))
        }

        val complaintSnapshot = FBRef.complaintRef.get().await()

        for (snapshot in complaintSnapshot.children) {
            if (snapshot.key == complaintId) {
                val targetComplaintId = snapshot.getValue(ComplaintEntity::class.java)
                if (targetComplaintId?.idList?.contains(id) == true) {
                    return "이미 신고한 사용자입니다."
                } else {
                    FBRef.complaintRef.child(complaintId).setValue(
                        targetComplaintId?.copy(
                            idList = targetComplaintId.idList.orEmpty().toMutableList() + id
                        )
                    )
                    return "신고했습니다."
                }
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
}