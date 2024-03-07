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

    suspend fun getUserId(
        name: String
    ) : String

    suspend fun getReview(
        id: String
    ) : List<ReviewEntity>
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

    override suspend fun getUserId(name: String): String {
        val userSnapshot = FBRef.userRef.get().await()

        for (snapshot in userSnapshot.children) {
            val user = snapshot.getValue(UserEntity::class.java)
            if (user?.userName == name) {
                return snapshot.key!!
            }
        }

        return ""
    }

    override suspend fun getReview(id: String): List<ReviewEntity> {
        val user = getUser(id)

        val reviewsSnapshot = FBRef.reviewRef.get().await()

        val targetReviewList: MutableList<ReviewEntity> = mutableListOf()
        user?.reviewIdList?.forEach { reviewId ->
            for (snapshot in reviewsSnapshot.children) {
                if (snapshot.key == reviewId) {
                    val review = snapshot.getValue(ReviewEntity::class.java)
                    if (review != null) targetReviewList.add(review)
                    break
                }
            }
        }

        return targetReviewList
    }
}