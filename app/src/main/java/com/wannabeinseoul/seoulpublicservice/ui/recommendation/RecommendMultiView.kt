//package com.wannabeinseoul.seoulpublicservice.ui.recommendation
//
//sealed interface RecommendMultiView {
//
//    enum class RecommendType {
//        NEXTWEEK, DISABLED, TEENAGER, AREA,
////            LOADING,
//    }
//
//    val viewType: RecommendType
//
//    data class NextWeekRecommendation(val data: SealedMulti.Recommendation) : RecommendMultiView {
//        override val viewType = RecommendType.NEXTWEEK
//    }
//
//    data class DisabledRecommendation(val data: SealedMulti.Recommendation) : RecommendMultiView {
//        override val viewType = RecommendType.DISABLED
//    }
//
//    data class TeenagerRecommendation(val data: SealedMulti.Recommendation) : RecommendMultiView {
//        override val viewType = RecommendType.TEENAGER
//    }
//
//    data class AreaRecommendation(val data: SealedMulti.Recommendation) : RecommendMultiView {
//        override val viewType = RecommendType.AREA
//    }
//}