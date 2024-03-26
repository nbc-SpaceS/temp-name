package com.wannabeinseoul.seoulpublicservice.kma.midTemp

data class TemperatureDTO(
    val response: Response
) {
    companion object {
        fun emptyTemp() = TemperatureDTO(
            Response(
                Body(
                    "",
                    Items(emptyList()),
                    0,
                    0,
                    0
                    ),
                Header(
                    "",
                    ""
                )
            )
        )
    }
}
data class Response(
    val body: Body,
    val header: Header
)
data class Items(
    val item: List<Item>
)
data class Item(
    val regId: String,
    val taMax10: Int,
    val taMax10High: Int,
    val taMax10Low: Int,
    val taMax3: Int,
    val taMax3High: Int,
    val taMax3Low: Int,
    val taMax4: Int,
    val taMax4High: Int,
    val taMax4Low: Int,
    val taMax5: Int,
    val taMax5High: Int,
    val taMax5Low: Int,
    val taMax6: Int,
    val taMax6High: Int,
    val taMax6Low: Int,
    val taMax7: Int,
    val taMax7High: Int,
    val taMax7Low: Int,
    val taMax8: Int,
    val taMax8High: Int,
    val taMax8Low: Int,
    val taMax9: Int,
    val taMax9High: Int,
    val taMax9Low: Int,
    val taMin10: Int,
    val taMin10High: Int,
    val taMin10Low: Int,
    val taMin3: Int,
    val taMin3High: Int,
    val taMin3Low: Int,
    val taMin4: Int,
    val taMin4High: Int,
    val taMin4Low: Int,
    val taMin5: Int,
    val taMin5High: Int,
    val taMin5Low: Int,
    val taMin6: Int,
    val taMin6High: Int,
    val taMin6Low: Int,
    val taMin7: Int,
    val taMin7High: Int,
    val taMin7Low: Int,
    val taMin8: Int,
    val taMin8High: Int,
    val taMin8Low: Int,
    val taMin9: Int,
    val taMin9High: Int,
    val taMin9Low: Int
)
data class Header(
    val resultCode: String,
    val resultMsg: String
)
data class Body(
    val dataType: String,
    val items: Items,
    val numOfRows: Int,
    val pageNo: Int,
    val totalCount: Int
)