package com.wannabeinseoul.seoulpublicservice.databases.firebase

import kotlinx.coroutines.tasks.await

interface UserRepository {
    fun addUser(
        id: String,
        user: UserEntity
    )

    suspend fun addUserReview(
        id: String,
        svcId: String
    )

    suspend fun getUser(
        id: String
    ) : UserEntity?
}

class UserRepositoryImpl: UserRepository {
    override fun addUser(id: String, user: UserEntity) {
        FBRef.userRef.child(id).setValue(user)
    }

    override suspend fun addUserReview(id: String, reviewId: String) {
        val user = getUser(id)

        user?.copy(
            reviewIdList = user.reviewIdList.orEmpty().toMutableList() + reviewId
        )?.let { addUser(id, it) }
    }

    override suspend fun getUser(id: String): UserEntity? {
        val userSnapshot = FBRef.userRef.get().await()

        var targetUser: UserEntity? = null
        for (snapshot in userSnapshot.children) {
            if (snapshot.key == id) {
                targetUser = snapshot.getValue(UserEntity::class.java)
                break
            }
        }

        return targetUser
    }
}