package com.wannabeinseoul.seoulpublicservice.ui.recommendation

data class RecommendationHorizontalData(
    val keyword: String,
    val title: String,
    val list: List<RecommendationData>,
)
