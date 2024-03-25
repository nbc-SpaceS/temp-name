package com.wannabeinseoul.seoulpublicservice.kma.temperature

import com.wannabeinseoul.seoulpublicservice.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TempApiService {
    @GET("MidFcstInfoService/getMidTa")
    suspend fun getTemp(
        @Query("ServiceKey") serviceKey: String = BuildConfig.TEMP_API_KEY,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("dataType") dataType: String,
        @Query("regId") regId: String,
        @Query("tmFc") tmFc: String
    ): Response<TemperatureDTO>
}