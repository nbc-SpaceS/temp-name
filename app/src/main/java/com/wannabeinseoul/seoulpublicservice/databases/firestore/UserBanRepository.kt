package com.wannabeinseoul.seoulpublicservice.databases.firestore

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.tasks.await

interface UserBanRepository {
    suspend fun getBanList(): List<String>
}

class UserBanRepositoryImpl : UserBanRepository {

    private val fireStore = Firebase.firestore

    override suspend fun getBanList(): List<String> =
        fireStore.collection("userBan").get().await().toObjects(String::class.java)
}