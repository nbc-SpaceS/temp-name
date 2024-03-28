package com.wannabeinseoul.seoulpublicservice.kma.midTemp

data class MidTemp(
    val response: Response? = null
)
data class Response(
    val body: Body? = null,
    val header: Header? = null
)
data class Items(
    val item: List<Item>? = null
)
data class Item(
    val regId: String? = null,
    val taMax10: Int? = null,
    val taMax10High: Int? = null,
    val taMax10Low: Int? = null,
    val taMax3: Int? = null,
    val taMax3High: Int? = null,
    val taMax3Low: Int? = null,
    val taMax4: Int? = null,
    val taMax4High: Int? = null,
    val taMax4Low: Int? = null,
    val taMax5: Int? = null,
    val taMax5High: Int? = null,
    val taMax5Low: Int? = null,
    val taMax6: Int? = null,
    val taMax6High: Int? = null,
    val taMax6Low: Int? = null,
    val taMax7: Int? = null,
    val taMax7High: Int? = null,
    val taMax7Low: Int? = null,
    val taMax8: Int? = null,
    val taMax8High: Int? = null,
    val taMax8Low: Int? = null,
    val taMax9: Int? = null,
    val taMax9High: Int? = null,
    val taMax9Low: Int? = null,
    val taMin10: Int? = null,
    val taMin10High: Int? = null,
    val taMin10Low: Int? = null,
    val taMin3: Int? = null,
    val taMin3High: Int? = null,
    val taMin3Low: Int? = null,
    val taMin4: Int? = null,
    val taMin4High: Int? = null,
    val taMin4Low: Int? = null,
    val taMin5: Int? = null,
    val taMin5High: Int? = null,
    val taMin5Low: Int? = null,
    val taMin6: Int? = null,
    val taMin6High: Int? = null,
    val taMin6Low: Int? = null,
    val taMin7: Int? = null,
    val taMin7High: Int? = null,
    val taMin7Low: Int? = null,
    val taMin8: Int? = null,
    val taMin8High: Int? = null,
    val taMin8Low: Int? = null,
    val taMin9: Int? = null,
    val taMin9High: Int? = null,
    val taMin9Low: Int? = null,
)
data class Header(
    val resultCode: String? = null,
    val resultMsg: String? = null
)
data class Body(
    val dataType: String? = null,
    val items: Items? = null,
    val numOfRows: Int? = null,
    val pageNo: Int? = null,
    val totalCount: Int? = null
)