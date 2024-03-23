package com.wannabeinseoul.seoulpublicservice.kma.temperature

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface TempApiService {
    @GET("MidFcstInfoService/getMidTa")
    suspend fun getTemp(
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("dataType") dataType: String,
        @Query("regId") regId: String,
        @Query("tmFc") tmFc: String
    ): Response<TemperatureDTO>
}