package com.wannabeinseoul.seoulpublicservice.databases.firebase

import android.net.Uri
import kotlinx.coroutines.tasks.await

interface UserProfileRepository {
    suspend fun uploadProfileImage(userId: String, uri: Uri): String
}

class UserProfileRepositoryImpl: UserProfileRepository {
    override suspend fun uploadProfileImage(userId: String, uri: Uri): String {
        return try {
            val uploadTask = FBRef.userProfileRef.child("${userId}.png").putFile(uri)
            uploadTask.await()
            FBRef.userProfileRef.child("${userId}.png").downloadUrl.await().toString()
        } catch (e: Exception) {
            ""
        }
    }
}