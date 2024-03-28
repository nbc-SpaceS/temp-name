package com.wannabeinseoul.seoulpublicservice.weather

import com.wannabeinseoul.seoulpublicservice.util.DLog

data class WeatherShort(
    val sky: Int = 0,
    val tmp: Int = 0,
    val pop: Int = 0
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
            else -> 4.apply {
                DLog.e(
                    "ShortMidMapper",
                    "midToShort weatherMid.sky: ${weatherMid.sky}"
                )
            }
        }
        return WeatherShort(
            sky = change,
            tmp = weatherMid.tmp,
            pop = weatherMid.pop
        )
    }
}