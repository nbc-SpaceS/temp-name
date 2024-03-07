package com.wannabeinseoul.seoulpublicservice.databases.firebase

import kotlinx.coroutines.tasks.await

interface UserBanRepository {
    suspend fun getBanList(): List<String>
}

class UserBanRepositoryImpl: UserBanRepository {
    override suspend fun getBanList(): List<String> {
        return FBRef.userBanRef.get().await().children.map { it.value.toString() }
    }
}