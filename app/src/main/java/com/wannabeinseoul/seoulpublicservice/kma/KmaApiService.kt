package com.wannabeinseoul.seoulpublicservice.kma

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface KmaApiService {
    @GET("getMidLandFcst")
    suspend fun getMidLandFcst(
        @Query("serviceKey") serviceKey: String,
        @Query("numOfRows") numOfRows: Int,
        @Query("pageNo") pageNo: Int,
        @Query("dataType") dataType: String,
        @Query("regId") regId: String,
        @Query("tmFc") tmFc: String
    ): Response<KmaMidLandFcstDto>
}