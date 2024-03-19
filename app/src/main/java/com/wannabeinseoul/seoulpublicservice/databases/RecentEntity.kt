package com.wannabeinseoul.seoulpublicservice.databases


/**
 * @property RecentEntity 최근 검색한 아이템 테이블
 * @param DATETIME 아이템을 검색한 날짜 & 시간
 * @param SVCID 서비스 ID
 * @param AREANM 지역명
 * @param SVCNM 서비스 이름
 * @param IMGURL 이미지 URL
 * @param SVCSTATNM 서비스 상태
 * @param PAYATNM 요금
 * @param MINCLASSNM 소분류명
 */

data class RecentEntity(
    val DATETIME: String,
    val SVCID: String,
    val AREANM: String,
    val SVCNM: String,
    val IMGURL: String,
    val SVCSTATNM: String,
    val PAYATNM: String,
    val MINCLASSNM: String
)
