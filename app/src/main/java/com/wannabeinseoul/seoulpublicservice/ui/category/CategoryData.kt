package com.wannabeinseoul.seoulpublicservice.ui.category

import com.wannabeinseoul.seoulpublicservice.databases.ReservationEntity

data class CategoryData(
    val imageUrl: String, // 이미지 URL
    val serviceName: String, // 서비스 이름
    val placeName: String, // 장소 이름
    val payType: String, // 지불 유형
    val areaName: String, // 지역 이름
    val isReservationAvailable: String, // 예약 가능 여부
    val svcid: String // 서비스 ID
)

fun ReservationEntity.convertToCategoryData() = CategoryData(
    imageUrl = this.IMGURL,
    serviceName = this.SVCNM,
    placeName = this.PLACENM,
    payType = this.PAYATNM,
    areaName = this.AREANM,
    isReservationAvailable = this.SVCSTATNM,
    svcid = this.SVCID
)

fun List<ReservationEntity>.convertToCategoryDataList(): List<CategoryData> =
    this.map { it.convertToCategoryData() }


//fun Row.convertToCategoryData() = CategoryData(
//    imageUrl = this.imgurl,
//    serviceName = this.svcnm,
//    placeName = this.placenm,
//    payType = this.payatnm,
//    areaName = this.areanm,
//    isReservationAvailable = this.svcstatnm,
//    svcid = this.svcid
//)
//
//fun List<Row>.convertToCategoryDataList(): List<CategoryData> =
//    this.map { it.convertToCategoryData() }
