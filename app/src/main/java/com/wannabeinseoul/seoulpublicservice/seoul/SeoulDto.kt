package com.wannabeinseoul.seoulpublicservice.seoul

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
) {
    companion object {
        fun new(
            div: String = "",
            service: String = "",
            gubun: String = "",
            svcid: String = "",
            maxclassnm: String = "",
            minclassnm: String = "",
            svcstatnm: String = "",
            svcnm: String = "",
            payatnm: String = "",
            placenm: String = "",
            usetgtinfo: String = "",
            svcurl: String = "",
            x: String = "",
            y: String = "",
            svcopnbgndt: String = "",
            svcopnenddt: String = "",
            rcptbgndt: String = "",
            rcptenddt: String = "",
            areanm: String = "",
            imgurl: String = "",
            dtlcont: String = "",
            telno: String = "",
            vMax: String = "",
            vMin: String = "",
            revstdday: Long = 0,
            revstddaynm: String = ""
        ) = Row(
            div,
            service,
            gubun,
            svcid,
            maxclassnm,
            minclassnm,
            svcstatnm,
            svcnm,
            payatnm,
            placenm,
            usetgtinfo,
            svcurl,
            x,
            y,
            svcopnbgndt,
            svcopnenddt,
            rcptbgndt,
            rcptenddt,
            areanm,
            imgurl,
            dtlcont,
            telno,
            vMax,
            vMin,
            revstdday,
            revstddaynm,
        )
    }
}
