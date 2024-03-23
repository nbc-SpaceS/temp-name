package com.wannabeinseoul.seoulpublicservice.weather

data class WeatherShort(
    val sky: Int,
    val tmp: Int,
    val pop: Int
)

data class WeatherMid(
    val sky: String,
    val tmp: Int,
    val pop: Int
)

object ShortMidMapper {
    fun midToShort(weatherMid: WeatherMid): WeatherShort {
        val change = when {
            weatherMid.sky == "맑음" -> 1
            weatherMid.sky.contains("구름많") -> 3
            weatherMid.sky.contains("흐") -> 4
            else -> throw Exception()
        }
        return WeatherShort(
            sky = change,
            tmp = weatherMid.tmp,
            pop = weatherMid.pop
        )
    }
}