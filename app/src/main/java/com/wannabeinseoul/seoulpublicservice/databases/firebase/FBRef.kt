package com.wannabeinseoul.seoulpublicservice.databases.firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage

class FBRef {
    companion object {
        private val database = Firebase.database
        val reviewRef = database.getReference("review")
        val userRef = database.getReference("user")
        val serviceRef = database.getReference("service")
        val complaintRef = database.getReference("complaint")
        val userBanRef = database.getReference("userBan")

        private val storage = Firebase.storage
        val ref = storage.reference
        val userProfileRef = storage.getReference("userProfile")
    }
}