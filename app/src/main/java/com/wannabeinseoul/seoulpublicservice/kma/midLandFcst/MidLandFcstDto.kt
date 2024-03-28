package com.wannabeinseoul.seoulpublicservice.kma.midLandFcst

import com.google.gson.annotations.SerializedName

data class KmaMidLandFcstDto(
    @SerializedName("response")
    val response: KmaMidLandFcstDtoResponse? = null
)

data class KmaMidLandFcstDtoResponse(
    @SerializedName("header")
    val header: Header? = null,

    @SerializedName("body")
    val body: Body? = null
)

data class Header(
    @SerializedName("resultCode")
    val resultCode: String? = null,

    @SerializedName("resultMsg")
    val resultMsg: String? = null
)

data class Body(
    @SerializedName("dataType")
    val dataType: String? = null,

    @SerializedName("items")
    val items: Items? = null,

    @SerializedName("pageNo")
    val pageNo: Int? = null,

    @SerializedName("numOfRows")
    val numOfRows: Int? = null,

    @SerializedName("totalCount")
    val totalCount: Int? = null
)

data class Items(
    @SerializedName("item")
    val itemList: List<Item>? = null
)

data class Item(
    @SerializedName("regId")
    val regId: String? = null,

    @SerializedName("rnSt3Am")
    val rnSt3Am: Int? = null,

    @SerializedName("rnSt3Pm")
    val rnSt3Pm: Int? = null,

    @SerializedName("rnSt4Am")
    val rnSt4Am: Int? = null,

    @SerializedName("rnSt4Pm")
    val rnSt4Pm: Int? = null,

    @SerializedName("rnSt5Am")
    val rnSt5Am: Int? = null,

    @SerializedName("rnSt5Pm")
    val rnSt5Pm: Int? = null,

    @SerializedName("rnSt6Am")
    val rnSt6Am: Int? = null,

    @SerializedName("rnSt6Pm")
    val rnSt6Pm: Int? = null,

    @SerializedName("rnSt7Am")
    val rnSt7Am: Int? = null,

    @SerializedName("rnSt7Pm")
    val rnSt7Pm: Int? = null,

    @SerializedName("rnSt8")
    val rnSt8: Int? = null,

    @SerializedName("rnSt9")
    val rnSt9: Int? = null,

    @SerializedName("rnSt10")
    val rnSt10: Int? = null,

    @SerializedName("wf3Am")
    val wf3Am: String? = null,

    @SerializedName("wf3Pm")
    val wf3Pm: String? = null,

    @SerializedName("wf4Am")
    val wf4Am: String? = null,

    @SerializedName("wf4Pm")
    val wf4Pm: String? = null,

    @SerializedName("wf5Am")
    val wf5Am: String? = null,

    @SerializedName("wf5Pm")
    val wf5Pm: String? = null,

    @SerializedName("wf6Am")
    val wf6Am: String? = null,

    @SerializedName("wf6Pm")
    val wf6Pm: String? = null,

    @SerializedName("wf7Am")
    val wf7Am: String? = null,

    @SerializedName("wf7Pm")
    val wf7Pm: String? = null,

    @SerializedName("wf8")
    val wf8: String? = null,

    @SerializedName("wf9")
    val wf9: String? = null,

    @SerializedName("wf10")
    val wf10: String? = null
)
