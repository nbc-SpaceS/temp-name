package com.wannabeinseoul.seoulpublicservice.databases.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import retrofit2.http.Url

interface UserRepository {
    fun addUser(user: UserEntity)

    fun addUserReview(
        id: String,
        svcId: String
    )

    fun getUser(
        id: String,
        listener: (UserEntity) -> Unit
    )
}

class UserRepositoryImpl: UserRepository {
    override fun addUser(user: UserEntity) {
        FBRef.userRef.child(user.userId ?: "").setValue(user)
    }

    override fun addUserReview(id: String, svcId: String) {
        getUser(id) {
            addUser(it.copy(
                reviewServiceId = it.reviewServiceId.orEmpty().toMutableList() + svcId
            ))
        }
    }

    override fun getUser(id: String, listener: (UserEntity) -> Unit) {
        FBRef.userRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val hashMap = snapshot.value as HashMap<*, *>

                listener(UserEntity(
                    hashMap["userId"] as String,
                    hashMap["userName"] as String,
                    hashMap["userImage"] as String,
                    hashMap["reviewServiceId"] as List<String>?
                ))
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}