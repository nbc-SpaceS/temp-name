package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import androidx.core.text.HtmlCompat
import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity

data class RecommendationData(
    val payType: String,
    val areaName: String,
    val placeName: String,
    val svcstatnm: String,
    val imageUrl: String,
    val svcid: String,
    val usetgtinfo: String,
    var reviewCount: Int,
    val serviceName: String,
) {
    val decodedServiceName: String =
        HtmlCompat.fromHtml(serviceName, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
}

fun ReservationEntity.convertToRecommendationData() = RecommendationData(
    payType = this.PAYATNM,
    areaName = this.AREANM,
    placeName = this.PLACENM,
    svcstatnm = this.SVCSTATNM,
    imageUrl = this.IMGURL,
    svcid = this.SVCID,
    usetgtinfo = this.USETGTINFO,
    reviewCount = 0,
    serviceName = this.SVCNM,
)

fun List<ReservationEntity>.convertToRecommendationDataList() =
    this.map { it.convertToRecommendationData() }
