package com.wannabeinseoul.seoulpublicservice.databases.firestore

import android.net.Uri
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import kotlinx.coroutines.tasks.await

interface UserProfileRepository {
    suspend fun uploadProfileImage(userId: String, uri: Uri): String
}

class UserProfileRepositoryImpl: UserProfileRepository {

    private val storage = Firebase.storage
    private val ref = storage.reference
    private val userProfileRef = storage.getReference("userProfile")

    override suspend fun uploadProfileImage(userId: String, uri: Uri): String {
        return try {
            val uploadTask = userProfileRef.child("${userId}.png").putFile(uri)
            uploadTask.await()
            ref.child("userProfile/${userId}.png").downloadUrl.await().toString()
        } catch (e: Exception) {
            ""
        }
    }
}