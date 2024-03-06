package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import com.wannabeinseoul.seoulpublicservice.seoul.Row

data class RecommendationData(
    val payType: String,
    val areaName: String,
    val placeName: String,
    val svcstatnm: String,
    val imageUrl: String,
    val svcid: String,
)

fun Row.convertToRecommendationData() = RecommendationData(
    payType = this.payatnm,
    areaName = this.areanm,
    placeName = this.placenm,
    svcstatnm = this.svcstatnm,
    imageUrl = this.imgurl,
    svcid = this.svcid,
)

fun List<Row>.convertToRecommendationDataList() =
    this.map { it.convertToRecommendationData() }


//    private fun convertRowToRecommendation(row: Row): RecommendationData = RecommendationData(
//        payType = row.payatnm,
//        areaName = row.areanm,
//        placeName = row.placenm,
//        svcstatnm = row.svcstatnm,
//        imageUrl = row.imgurl,
//    )
//
//    private fun convertRowsToRecommendations(rows: List<Row>): List<RecommendationData> =
//        rows.map { convertRowToRecommendation(it) }

