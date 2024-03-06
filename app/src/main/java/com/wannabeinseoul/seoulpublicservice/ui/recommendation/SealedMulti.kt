//package com.wannabeinseoul.seoulpublicservice.ui.recommendation
//
//
//sealed interface SealedMulti {
//
//    enum class Type {
//        RECOMMENDATION
//    }
//
//    val viewType: Type
//
//    data class Recommendation(
//        val payType: String,
//        val areaName: String,
//        val placeName: String,
//        val isReservationAvailable: Boolean,
//        val imageUrl: String,
//    ) : SealedMulti {
//        override val viewType: Type = Type.RECOMMENDATION
//    }
//}