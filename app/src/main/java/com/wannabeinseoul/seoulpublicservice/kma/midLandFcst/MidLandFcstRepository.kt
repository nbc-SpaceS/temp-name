package com.wannabeinseoul.seoulpublicservice.kma.midLandFcst

import retrofit2.Response

interface KmaRepository {
    suspend fun getMidLandFcst(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<KmaMidLandFcstDto>
}

class KmaRepositoryImpl(
    private val midLandFcstApiService: MidLandFcstApiService
) : KmaRepository {
    override suspend fun getMidLandFcst(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<KmaMidLandFcstDto> {
        val response = midLandFcstApiService.getMidLandFcst(numOfRows = numOfRows, pageNo = pageNo, dataType = dataType, regId = regId, tmFc = tmFc)
        return response
    }
}