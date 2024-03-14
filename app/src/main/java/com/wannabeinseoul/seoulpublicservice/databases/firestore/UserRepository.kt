package com.wannabeinseoul.seoulpublicservice.databases.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.wannabeinseoul.seoulpublicservice.databases.entity.ReviewEntity
import com.wannabeinseoul.seoulpublicservice.databases.entity.UserEntity
import kotlinx.coroutines.tasks.await

interface UserRepository {
    fun addUser(
        id: String,
        user: UserEntity
    )

    suspend fun updateUserName(
        id: String,
        name: String
    )

    suspend fun updateUserProfileImage(
        id: String,
        profileImage: String
    )

    suspend fun updateAll(
        id: String,
        name: String = "",
        profileImage: String = ""
    )

    suspend fun addUserReview(
        id: String,
        reviewId: String
    )

    suspend fun getUser(
        id: String
    ): UserEntity?

    suspend fun getUserId(
        name: String
    ): String

    suspend fun getReview(
        id: String
    ): List<ReviewEntity>
}

class UserRepositoryImpl : UserRepository {
    private val fireStore = Firebase.firestore

    override fun addUser(id: String, user: UserEntity) {
        fireStore.collection("user").document(id).set(user)
    }

    override suspend fun updateUserName(id: String, name: String) {
        fireStore.collection("user").document(id).update("userName", name)
    }

    override suspend fun updateUserProfileImage(id: String, profileImage: String) {
        fireStore.collection("user").document(id).update("userProfileImage", profileImage)
    }

    override suspend fun updateAll(id: String, name: String, profileImage: String) {
        fireStore.collection("user").document(id)
            .update(mapOf("userName" to name, "userProfileImage" to profileImage))
    }

    override suspend fun addUserReview(id: String, reviewId: String) {
        val user = getUser(id)
        fireStore.collection("user").document(id)
            .update("reviewIdList", user?.reviewIdList.orEmpty().toMutableList() + reviewId)
    }

    override suspend fun getUser(id: String): UserEntity? =
        fireStore.collection("user").document(id).get().await().toObject(UserEntity::class.java)

    override suspend fun getUserId(name: String): String {
        val user =
            fireStore.collection("user").whereEqualTo("userName", name).limit(1).get().await()
        return user.documents[0].id
    }

    override suspend fun getReview(id: String): List<ReviewEntity> =
        fireStore.collection("review").whereEqualTo("userId", id).get().await()
            .toObjects(ReviewEntity::class.java)
}