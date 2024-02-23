package com.example.seoulpublicservice.seoul

import com.google.gson.annotations.SerializedName

/** TvYeyakCOllect 응답 dto */
data class SeoulDto(
    val tvYeyakCOllect: TvYeyakCOllect
)

data class TvYeyakCOllect(
    @SerializedName("list_total_count")
    val listTotalCount: Long,

    @SerializedName("RESULT")
    val result: Result,

    @SerializedName("row")
    val rowList: List<Row>
)


data class Result(
    @SerializedName("CODE")
    val code: String,

    @SerializedName("MESSAGE")
    val message: String
)


data class Row(
    @SerializedName("DIV")
    val div: String,

    @SerializedName("SERVICE")
    val service: String,

    @SerializedName("GUBUN")
    val gubun: String,

    @SerializedName("SVCID")
    val svcid: String,

    @SerializedName("MAXCLASSNM")
    val maxclassnm: String,

    @SerializedName("MINCLASSNM")
    val minclassnm: String,

    @SerializedName("SVCSTATNM")
    val svcstatnm: String,

    @SerializedName("SVCNM")
    val svcnm: String,

    @SerializedName("PAYATNM")
    val payatnm: String,

    @SerializedName("PLACENM")
    val placenm: String,

    @SerializedName("USETGTINFO")
    val usetgtinfo: String,

    @SerializedName("SVCURL")
    val svcurl: String,

    @SerializedName("X")
    val x: String,

    @SerializedName("Y")
    val y: String,

    @SerializedName("SVCOPNBGNDT")
    val svcopnbgndt: String,

    @SerializedName("SVCOPNENDDT")
    val svcopnenddt: String,

    @SerializedName("RCPTBGNDT")
    val rcptbgndt: String,

    @SerializedName("RCPTENDDT")
    val rcptenddt: String,

    @SerializedName("AREANM")
    val areanm: String,

    @SerializedName("IMGURL")
    val imgurl: String,

    @SerializedName("DTLCONT")
    val dtlcont: String,

    @SerializedName("TELNO")
    val telno: String,

    @SerializedName("V_MAX")
    val vMax: String,

    @SerializedName("V_MIN")
    val vMin: String,

    @SerializedName("REVSTDDAY")
    val revstdday: Long,

    @SerializedName("REVSTDDAYNM")
    val revstddaynm: String
)


//enum class Div(val value: String) {
//    @SerializedName("문화행사")
//    문화행사("문화행사"),
//
//    @SerializedName("시설대관")
//    시설대관("시설대관"),
//
//    @SerializedName("진료")
//    진료("진료"),
//
//    @SerializedName("체육시설")
//    체육시설("체육시설");
//}
//
//
//enum class Gubun(val value: String) {
//    @SerializedName("연계")
//    연계("연계"),
//
//    @SerializedName("자체")
//    자체("자체");
//}
//
//
//enum class Maxclassnm(val value: String) {
//    @SerializedName("공간시설")
//    공간시설("공간시설"),
//
//    @SerializedName("문화체험")
//    문화체험("문화체험"),
//
//    @SerializedName("진료복지")
//    진료복지("진료복지"),
//
//    @SerializedName("체육시설")
//    체육시설("체육시설");
//}
//
//
//enum class Payatnm(val value: String) {
//    @SerializedName("무료")
//    무료("무료"),
//
//    @SerializedName("유료")
//    유료("유료"),
//
//    @SerializedName("유료(요금안내문의)")
//    유료요금안내문의("유료(요금안내문의)");
//}
//
//
//enum class Revstddaynm(val value: String) {
//    @SerializedName("")
//    Empty(""),
//
//    @SerializedName("이용기간시작일")
//    이용기간시작일("이용기간시작일"),
//
//    @SerializedName("이용일")
//    이용일("이용일"),
//
//    @SerializedName("접수종료일")
//    접수종료일("접수종료일");
//}
//
//
//enum class Svcstatnm(val value: String) {
//    @SerializedName("안내중")
//    안내중("안내중"),
//
//    @SerializedName("예약마감")
//    예약마감("예약마감"),
//
//    @SerializedName("예약일시중지")
//    예약일시중지("예약일시중지"),
//
//    @SerializedName("접수종료")
//    접수종료("접수종료"),
//
//    @SerializedName("접수중")
//    접수중("접수중");
//}
//
//
//enum class VMin(val value: String) {
//    @SerializedName("")
//    Empty(""),
//
//    @SerializedName("00:00")
//    The0000("00:00"),
//
//    @SerializedName("01:00")
//    The0100("01:00"),
//
//    @SerializedName("05:00")
//    The0500("05:00"),
//
//    @SerializedName("06:00")
//    The0600("06:00"),
//
//    @SerializedName("07:00")
//    The0700("07:00"),
//
//    @SerializedName("07:20")
//    The0720("07:20"),
//
//    @SerializedName("08:00")
//    The0800("08:00"),
//
//    @SerializedName("09:00")
//    The0900("09:00"),
//
//    @SerializedName("09:30")
//    The0930("09:30"),
//
//    @SerializedName("09:40")
//    The0940("09:40"),
//
//    @SerializedName("10:00")
//    The1000("10:00"),
//
//    @SerializedName("10:30")
//    The1030("10:30"),
//
//    @SerializedName("11:00")
//    The1100("11:00"),
//
//    @SerializedName("12:00")
//    The1200("12:00"),
//
//    @SerializedName("13:00")
//    The1300("13:00"),
//
//    @SerializedName("13:30")
//    The1330("13:30"),
//
//    @SerializedName("14:00")
//    The1400("14:00"),
//
//    @SerializedName("15:00")
//    The1500("15:00"),
//
//    @SerializedName("16:00")
//    The1600("16:00"),
//
//    @SerializedName("16:30")
//    The1630("16:30"),
//
//    @SerializedName("17:00")
//    The1700("17:00"),
//
//    @SerializedName("18:00")
//    The1800("18:00"),
//
//    @SerializedName("19:00")
//    The1900("19:00"),
//
//    @SerializedName("19:30")
//    The1930("19:30");
//}
