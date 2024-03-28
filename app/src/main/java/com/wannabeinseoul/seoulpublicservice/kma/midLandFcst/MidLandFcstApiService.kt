package com.wannabeinseoul.seoulpublicservice.kma.midLandFcst

import com.wannabeinseoul.seoulpublicservice.BuildConfig
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface MidLandFcstApiService {
    @GET("MidFcstInfoService/getMidLandFcst")
    suspend fun getMidLandFcst(
        @Query("ServiceKey") serviceKey: String = BuildConfig.KMA_API_KEY,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("dataType") dataType: String,
        @Query("regId") regId: String,
        @Query("tmFc") tmFc: String
    ): Response<KmaMidLandFcstDto>
}
