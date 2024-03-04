package com.wannabeinseoul.seoulpublicservice.ui.recommendation


sealed interface SealedMulti {

    enum class Type {
        RECOMMENDATION
    }

    val viewType: Type

    data class Recommendation(
        val payType: String,
        val areaName: String,
        val placeName: String,
        val isReservationAvailable: String,
        val imageUrl: String,
        val serviceList: String
    ) : SealedMulti {
        override val viewType: Type = Type.RECOMMENDATION
    }
}