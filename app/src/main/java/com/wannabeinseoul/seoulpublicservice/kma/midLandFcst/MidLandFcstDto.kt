package com.wannabeinseoul.seoulpublicservice.kma.midLandFcst

import com.google.gson.annotations.SerializedName

data class KmaMidLandFcstDto(
    @SerializedName("response")
    val response: Response
)

data class Response(
    @SerializedName("header")
    val header: Header,

    @SerializedName("body")
    val body: Body
)

data class Header(
    @SerializedName("resultCode")
    val resultCode: String,

    @SerializedName("resultMsg")
    val resultMsg: String
)

data class Body(
    @SerializedName("dataType")
    val dataType: String,

    @SerializedName("items")
    val items: Items,

    @SerializedName("pageNo")
    val pageNo: Int,

    @SerializedName("numOfRows")
    val numOfRows: Int,

    @SerializedName("totalCount")
    val totalCount: Int
)

data class Items(
    @SerializedName("item")
    val itemList: List<Item>
)

data class Item(
    @SerializedName("regId")
    val regId: String,

    @SerializedName("rnSt3Am")
    val rnSt3Am: Int,

    @SerializedName("rnSt3Pm")
    val rnSt3Pm: Int,

    @SerializedName("rnSt4Am")
    val rnSt4Am: Int,

    @SerializedName("rnSt4Pm")
    val rnSt4Pm: Int,

    @SerializedName("rnSt5Am")
    val rnSt5Am: Int,

    @SerializedName("rnSt5Pm")
    val rnSt5Pm: Int,

    @SerializedName("rnSt6Am")
    val rnSt6Am: Int,

    @SerializedName("rnSt6Pm")
    val rnSt6Pm: Int,

    @SerializedName("rnSt7Am")
    val rnSt7Am: Int,

    @SerializedName("rnSt7Pm")
    val rnSt7Pm: Int,

    @SerializedName("rnSt8")
    val rnSt8: Int,

    @SerializedName("rnSt9")
    val rnSt9: Int,

    @SerializedName("rnSt10")
    val rnSt10: Int,

    @SerializedName("wf3Am")
    val wf3Am: String,

    @SerializedName("wf3Pm")
    val wf3Pm: String,

    @SerializedName("wf4Am")
    val wf4Am: String,

    @SerializedName("wf4Pm")
    val wf4Pm: String,

    @SerializedName("wf5Am")
    val wf5Am: String,

    @SerializedName("wf5Pm")
    val wf5Pm: String,

    @SerializedName("wf6Am")
    val wf6Am: String,

    @SerializedName("wf6Pm")
    val wf6Pm: String,

    @SerializedName("wf7Am")
    val wf7Am: String,

    @SerializedName("wf7Pm")
    val wf7Pm: String,

    @SerializedName("wf8")
    val wf8: String,

    @SerializedName("wf9")
    val wf9: String,

    @SerializedName("wf10")
    val wf10: String
)
