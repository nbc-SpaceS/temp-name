package com.example.seoulpublicservice.databases

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @property ReservationEntity 테이블 명
 * @param AREANM 지역명
 * @param DTLCONT 상세정보
 * @param GUBUN 서비스 구분
 * @param IMGURL 이미지 URL
 * @param MAXCLASSNM 대분류명
 * @param MINCLASSNM 소분류명
 * @param PAYATNM 결제방법
 * @param PLACENM 장소명
 * @param RCPTBGNDT 접수 시작 일시
 * @param RCPTENDDT 접수 종료 일시
 * @param REVSTDDAY 취소시간 기준일까지
 * @param REVSTDDAYNM 취소시간 기준정보
 * @param SVCID 서비스 ID / PrimaryKey(기본키)
 * @param SVCNM 서비스명
 * @param SVCOPNBGNDT 서비스 개시 시작 일시
 * @param SVCOPNENDDT 서비스 개시 종료 일시
 * @param SVCSTATNM  서비스 상태
 * @param SVCURL 서비스 바로가기 URL
 * @param TELNO 전화번호
 * @param USETGTINFO 서비스 대상
 * @param V_MAX 서비스 이용 종료시간
 * @param V_MIN 서비스 이용 시작시간
 * @param X 장소의 X 좌표
 * @param Y 장소의 Y 좌표
 */
@Entity
data class ReservationEntity(
    val AREANM: String,
    val DTLCONT: String,
    val GUBUN: String,
    val IMGURL: String,
    val MAXCLASSNM: String,
    val MINCLASSNM: String,
    val PAYATNM: String,
    val PLACENM: String,
    val RCPTBGNDT: String,
    val RCPTENDDT: String,
    val REVSTDDAY: String,
    val REVSTDDAYNM: String,
    @PrimaryKey val SVCID: String,
    val SVCNM: String,
    val SVCOPNBGNDT: String,
    val SVCOPNENDDT: String,
    val SVCSTATNM: String,
    val SVCURL: String,
    val TELNO: String,
    val USETGTINFO: String,
    val V_MAX: String,
    val V_MIN: String,
    val X: String,
    val Y: String
)