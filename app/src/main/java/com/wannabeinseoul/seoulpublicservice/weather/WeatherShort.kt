package com.wannabeinseoul.seoulpublicservice.weather

import android.util.Log

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
            else -> 4.apply {
                Log.e(
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