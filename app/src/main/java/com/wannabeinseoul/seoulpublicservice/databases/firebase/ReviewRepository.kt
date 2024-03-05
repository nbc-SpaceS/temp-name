package com.wannabeinseoul.seoulpublicservice.databases.firebase

import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener

interface ReviewRepository {
    fun addReview(review: ReviewEntity)

    fun getServiceReviews(
        id: String,
        listener: (List<ReviewEntity>) -> Unit
    )
}

class ReviewRepositoryImpl: ReviewRepository {
    override fun addReview(review: ReviewEntity) {
        FBRef.reviewRef.child(review.svcId ?: "").child(review.userId ?: "").setValue(review)
    }

    override fun getServiceReviews(
        id: String,
        listener: (List<ReviewEntity>) -> Unit
    ) {
        FBRef.reviewRef.child(id).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var serviceReviews = mutableListOf<ReviewEntity>()

                for (data in snapshot.children) {
                    val hashMap = data.value as HashMap<*, *>
                    serviceReviews.add(ReviewEntity(
                        hashMap["svcId"] as String,
                        hashMap["userId"] as String,
                        hashMap["content"] as String
                    ))
                }

                listener(serviceReviews)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })
    }
}