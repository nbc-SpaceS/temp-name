package com.wannabeinseoul.seoulpublicservice.weather

import android.util.Log

private const val JJTAG = "jj-WeatherShortRepository"

interface WeatherShortRepository {
    /**
     * @property getShortWeather 단기예보 요청
     * @param page 가져올 페이지 수
     * @param row 페이지 당 결과 수
     * @param date 발표일자
     * @param time 발표시각
     * @param x 예보지점의 X 좌표값
     * @param y 예보지점의 Y 좌표값
     */
    suspend fun getShortWeather(
        page: Int,
        row: Int,
        date: String,
        time: String,
        x: Int,
        y: Int
    ) : WeatherShortDTO
}

class WeatherShortRepositoryImpl(
    private val weatherApiService: WeatherApiService
) : WeatherShortRepository {
    override suspend fun getShortWeather(
        page: Int,
        row: Int,
        date: String,
        time: String,
        x: Int,
        y: Int
    ) = try {
        weatherApiService.getWeatherShort(
            page = page,
            row = row,
            date = date,
            time = time,
            x = x,
            y = y
        )
    } catch (e: Throwable) {
        Log.e(JJTAG, "getShortWeather page:$page, row:$row, date:$date, time:$time, x:$x, y:$y", e)
        WeatherShortDTO.new()
    }
}