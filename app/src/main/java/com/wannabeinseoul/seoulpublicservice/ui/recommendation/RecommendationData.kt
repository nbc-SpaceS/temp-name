package com.wannabeinseoul.seoulpublicservice.ui.recommendation

import androidx.core.text.HtmlCompat
import com.wannabeinseoul.seoulpublicservice.seoul.Row

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

fun Row.convertToRecommendationData() = RecommendationData(
    payType = this.payatnm,
    areaName = this.areanm,
    placeName = this.placenm,
    svcstatnm = this.svcstatnm,
    imageUrl = this.imgurl,
    svcid = this.svcid,
    usetgtinfo = this.usetgtinfo,
    reviewCount = 0,
    serviceName = this.svcnm,
)
//플레이스네임을 서비스스텟으로

fun List<Row>.convertToRecommendationDataList() =
    this.map { it.convertToRecommendationData() }
