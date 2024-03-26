package com.wannabeinseoul.seoulpublicservice.kma.midTemp

import com.wannabeinseoul.seoulpublicservice.BuildConfig
import retrofit2.http.GET
import retrofit2.http.Query

interface MidTempApiService {
    @GET("MidFcstInfoService/getMidTa")
    suspend fun getTemp(
        @Query("ServiceKey") serviceKey: String = BuildConfig.TEMP_API_KEY,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("dataType") dataType: String,
        @Query("regId") regId: String,
        @Query("tmFc") tmFc: String
    ): TemperatureDTO
}