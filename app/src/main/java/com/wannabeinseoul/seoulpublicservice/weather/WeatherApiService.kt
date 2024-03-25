package com.wannabeinseoul.seoulpublicservice.weather


import com.wannabeinseoul.seoulpublicservice.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query


interface WeatherApiService {
    @GET("VilageFcstInfoService_2.0/getVilageFcst")
    suspend fun getWeatherShort(
        @Query("ServiceKey") key: String = BuildConfig.WEATHER_SHORT_KEY,
        @Query("pageNo") page: Int,
        @Query("numOfRows") row: Int,
        @Query("dataType") type: String = "JSON",
        @Query("base_date") date: String,
        @Query("base_time") time: String,
        @Query("nx") x: Int,
        @Query("ny") y: Int
    ): WeatherShortDTO
}

//   /VilageFcstInfoService_2.0/getVilageFcst 단기
//   /MidFcstInfoService/getMidLandFcst