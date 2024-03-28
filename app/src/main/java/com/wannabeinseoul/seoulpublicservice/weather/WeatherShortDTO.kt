package com.wannabeinseoul.seoulpublicservice.weather

data class WeatherShortDTO(
    val response: WeatherShortDTOResponse? = null
)

data class WeatherShortDTOResponse(
    val body: Body? = null,
    val header: Header? = null
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

data class Item(
    val baseDate: String? = null,
    val baseTime: String? = null,
    val category: String? = null,
    val fcstDate: String? = null,
    val fcstTime: String? = null,
    val fcstValue: String? = null,
    val nx: Int? = null,
    val ny: Int? = null
)

data class Items(
    val item: List<Item>? = null
)
