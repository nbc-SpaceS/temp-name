package com.wannabeinseoul.seoulpublicservice.weather

data class WeatherShortDTO(
    val response: Response
) {
    companion object {
        fun new() = WeatherShortDTO(
            Response(
                Body(
                    "",
                    Items(emptyList()),
                    0,
                    0,
                    0,
                ),
                Header(
                    "",
                    "",
                )
            )
        )
    }
}

data class Response(
    val body: Body,
    val header: Header
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

data class Item(
    val baseDate: String,
    val baseTime: String,
    val category: String,
    val fcstDate: String,
    val fcstTime: String,
    val fcstValue: String,
    val nx: Int,
    val ny: Int
)

data class Items(
    val item: List<Item>
)