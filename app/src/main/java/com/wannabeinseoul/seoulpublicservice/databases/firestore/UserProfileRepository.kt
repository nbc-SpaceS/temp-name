package com.wannabeinseoul.seoulpublicservice.databases.firestore

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import com.wannabeinseoul.seoulpublicservice.util.toPngByteArray
import kotlinx.coroutines.tasks.await

interface UserProfileRepository {
    suspend fun uploadProfileImage(userId: String, uri: Uri): String
    suspend fun uploadProfileImage(userId: String, bitmap: Bitmap): String
}

class UserProfileRepositoryImpl(
    private val userRepository: UserRepository,
) : UserProfileRepository {

    private val storage = Firebase.storage
    private val ref = storage.reference
    private val userProfileRef = storage.getReference("userProfile")

    override suspend fun uploadProfileImage(userId: String, uri: Uri): String {
        return try {
            val uploadTask = userProfileRef.child("${userId}.png").putFile(uri)
            uploadTask.await()
            val uploadedUrl = ref.child("userProfile/${userId}.png").downloadUrl.await().toString()
            userRepository.updateUserProfileImage(userId, uploadedUrl)
            uploadedUrl
        } catch (e: Exception) {
            Log.e("jj-UserProfileRepository", "uploadProfileImage fail: $e")
            ""
        }
    }

    private suspend fun uploadProfileImage(userId: String, byteArray: ByteArray): String {
        return try {
            val uploadTask = userProfileRef.child("${userId}.png").putBytes(byteArray)
            uploadTask.await()
            val uploadedUrl = ref.child("userProfile/${userId}.png").downloadUrl.await().toString()
            userRepository.updateUserProfileImage(userId, uploadedUrl)
            uploadedUrl
        } catch (e: Exception) {
            Log.e("jj-UserProfileRepository", "uploadProfileImage fail: $e")
            ""
        }
    }

    override suspend fun uploadProfileImage(userId: String, bitmap: Bitmap): String =
        uploadProfileImage(userId, bitmap.toPngByteArray())

}
