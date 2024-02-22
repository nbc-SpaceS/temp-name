package com.example.seoulpublicservice.databases

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class ReservationEntity(    // Table명 = Data Class 이름
    val AREANM: String,             // 지역명
    val DTLCONT: String,            // 상세정보
    val GUBUN: String,              // 서비스 구분
    val IMGURL: String,             // 이미지 경로
    val MAXCLASSNM: String,         // 대분류명
    val MINCLASSNM: String,         // 소분류명
    val PAYATNM: String,            // 결제방법
    val PLACENM: String,            // 장소명
    val RCPTBGNDT: String,          // 접수 시작 일시
    val RCPTENDDT: String,          // 접수 종료 일시
    val REVSTDDAY: String,          // 취소시간 기준일까지
    val REVSTDDAYNM: String,        // 취소시간 기준정보
    @PrimaryKey val SVCID: String,  // 서비스 ID
    val SVCNM: String,              // 서비스명
    val SVCOPNBGNDT: String,        // 서비스 개시 시작 일시
    val SVCOPNENDDT: String,        // 서비스 개시 종료 일시
    val SVCSTATNM: String,          // 서비스 상태
    val SVCURL: String,             // 바로가기 URL
    val TELNO: String,              // 전화번호
    val USETGTINFO: String,         // 서비스 대상
    val V_MAX: String,              // 서비스 이용 종료시간
    val V_MIN: String,              // 서비스 이용 시작시간
    val X: String,                  // 장소 X 좌표
    val Y: String                   // 장소 Y 좌표
)