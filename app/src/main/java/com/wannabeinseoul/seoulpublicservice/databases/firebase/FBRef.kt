package com.wannabeinseoul.seoulpublicservice.databases.firebase

import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class FBRef {
    companion object {
        private val database = Firebase.database
        val reviewRef = database.getReference("review")
        val userRef = database.getReference("user")
        val serviceRef = database.getReference("service")
    }
}