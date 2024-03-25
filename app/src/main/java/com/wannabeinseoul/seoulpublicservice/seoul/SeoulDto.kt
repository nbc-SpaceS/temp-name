package com.wannabeinseoul.seoulpublicservice.seoul

import com.google.gson.annotations.SerializedName

/** TvYeyakCOllect 응답 dto */
data class SeoulDto(
    val tvYeyakCOllect: TvYeyakCOllect? = null
)

data class TvYeyakCOllect(
    @SerializedName("list_total_count")
    val listTotalCount: Long? = null,

    @SerializedName("RESULT")
    val result: Result? = null,

    @SerializedName("row")
    val rowList: List<Row>? = null
)


data class Result(
    @SerializedName("CODE")
    val code: String? = null,

    @SerializedName("MESSAGE")
    val message: String? = null
)


data class Row(
    @SerializedName("DIV")
    val div: String? = null,

    @SerializedName("SERVICE")
    val service: String? = null,

    @SerializedName("GUBUN")
    val gubun: String? = null,

    @SerializedName("SVCID")
    val svcid: String? = null,

    @SerializedName("MAXCLASSNM")
    val maxclassnm: String? = null,

    @SerializedName("MINCLASSNM")
    val minclassnm: String? = null,

    @SerializedName("SVCSTATNM")
    val svcstatnm: String? = null,

    @SerializedName("SVCNM")
    val svcnm: String? = null,

    @SerializedName("PAYATNM")
    val payatnm: String? = null,

    @SerializedName("PLACENM")
    val placenm: String? = null,

    @SerializedName("USETGTINFO")
    val usetgtinfo: String? = null,

    @SerializedName("SVCURL")
    val svcurl: String? = null,

    @SerializedName("X")
    val x: String? = null,

    @SerializedName("Y")
    val y: String? = null,

    @SerializedName("SVCOPNBGNDT")
    val svcopnbgndt: String? = null,

    @SerializedName("SVCOPNENDDT")
    val svcopnenddt: String? = null,

    @SerializedName("RCPTBGNDT")
    val rcptbgndt: String? = null,

    @SerializedName("RCPTENDDT")
    val rcptenddt: String? = null,

    @SerializedName("AREANM")
    val areanm: String? = null,

    @SerializedName("IMGURL")
    val imgurl: String? = null,

    @SerializedName("DTLCONT")
    val dtlcont: String? = null,

    @SerializedName("TELNO")
    val telno: String? = null,

    @SerializedName("V_MAX")
    val vMax: String? = null,

    @SerializedName("V_MIN")
    val vMin: String? = null,

    @SerializedName("REVSTDDAY")
    val revstdday: Long? = null,

    @SerializedName("REVSTDDAYNM")
    val revstddaynm: String? = null
)
