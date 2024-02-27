package com.example.seoulpublicservice.seoul

import com.google.gson.annotations.SerializedName

/** ListPublicReservationDetail 응답 dto */
data class SeoulDetailDto(
    @SerializedName("ListPublicReservationDetail")
    val listPublicReservationDetail: ListPublicReservationDetail
)

data class ListPublicReservationDetail(
    @SerializedName("list_total_count")
    val listTotalCount: Long,

    @SerializedName("RESULT")
    val result: DetailResult,

    @SerializedName("row")
    val rowList: List<DetailRow>
)

data class DetailResult(
    @SerializedName("CODE")
    val code: String,

    @SerializedName("MESSAGE")
    val message: String
)

data class DetailRow(
    @SerializedName("SVCID")
    val svcid: String,

    @SerializedName("SVCNM")
    val svcnm: String,

    @SerializedName("FEEGUIDURL")
    val feeguidurl: String,

    @SerializedName("SVCBEGINDT")
    val svcbegindt: String,

    @SerializedName("SVCENDDT")
    val svcenddt: String,

    @SerializedName("PLACESN")
    val placesn: String,

    @SerializedName("PLACENM")
    val placenm: String,

    @SerializedName("SUBPLACENM")
    val subplacenm: String,

    @SerializedName("PAYAT")
    val payat: String,

    @SerializedName("RCPTMTHD")
    val rcptmthd: String,

    @SerializedName("RCEPTMTH_NM")
    val rceptmthNm: String,

    @SerializedName("RCEPTBEGDT")
    val rceptbegdt: String,

    @SerializedName("RCEPTENDDT")
    val rceptenddt: String,

    @SerializedName("RCRPERCAP")
    val rcrpercap: Long,

    @SerializedName("UNITCODE")
    val unitcode: String,

    @SerializedName("UNICODE_NM")
    val unicodeNm: String,

    @SerializedName("SELMTHDCODE")
    val selmthdcode: String,

    @SerializedName("SELMTHDCODE_NM")
    val selmthdcodeNm: String,

    @SerializedName("SVCENDTELNO")
    val svcendtelno: String,

    @SerializedName("SVCENDUSRSN")
    val svcendusrsn: String,

    @SerializedName("ORGNM")
    val orgnm: String,

    @SerializedName("ONEREQMINPR")
    val onereqminpr: Long,

    @SerializedName("ONEREQMXMPR")
    val onereqmxmpr: Long,

    @SerializedName("SVCSTTUS")
    val svcsttus: String,

    @SerializedName("SVCSTTUS_NM")
    val svcsttusNm: String,

    @SerializedName("REVSTDDAY")
    val revstdday: String,

    @SerializedName("CODE")
    val code: String,

    @SerializedName("CODENM")
    val codenm: String,

    @SerializedName("SMCODE")
    val smcode: String,

    @SerializedName("SMCODE_NM")
    val smcodeNm: String,

    @SerializedName("WAITNUM")
    val waitnum: Long,

    @SerializedName("USETIMEUNITCODE")
    val usetimeunitcode: String,

    @SerializedName("USETIMEUNITCODE_NM")
    val usetimeunitcodeNm: String,

    @SerializedName("USEDAYSTDRCPTDAY")
    val usedaystdrcptday: String,

    @SerializedName("USEDAYSTDRCPTTIME")
    val usedaystdrcpttime: String,

    @SerializedName("RSVDAYSTDRCPTDAY")
    val rsvdaystdrcptday: String,

    @SerializedName("RSVDAYSTDRCPTTIME")
    val rsvdaystdrcpttime: String,

    @SerializedName("USELIMMINNOP")
    val uselimminnop: Long,

    @SerializedName("USELIMMAXNOP")
    val uselimmaxnop: Long,

    @SerializedName("EXTINFO")
    val extinfo: String,

    @SerializedName("X")
    val x: String,

    @SerializedName("Y")
    val y: String,

    @SerializedName("ADRES")
    val adres: String,

    @SerializedName("TELNO")
    val telno: String,

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

    @SerializedName("NOTICE")
    val notice: String,

    @SerializedName("IMG_PATH")
    val imgPath: String,

    @SerializedName("DTLCONT")
    val dtlcont: String,

    @SerializedName("V_MAX")
    val vMax: String,

    @SerializedName("V_MIN")
    val vMin: String,

    @SerializedName("REVSTDDAYNM")
    val revstddaynm: String
)
