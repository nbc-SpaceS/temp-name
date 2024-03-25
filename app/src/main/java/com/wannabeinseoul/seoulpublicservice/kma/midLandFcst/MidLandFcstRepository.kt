package com.wannabeinseoul.seoulpublicservice.kma.midLandFcst

import android.util.Log
import retrofit2.Response

interface KmaRepository {
    suspend fun getMidLandFcst(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<KmaMidLandFcstDto>?
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
    ): Response<KmaMidLandFcstDto>? {
        try {
            return midLandFcstApiService.getMidLandFcst(
                numOfRows = numOfRows,
                pageNo = pageNo,
                dataType = dataType,
                regId = regId,
                tmFc = tmFc
            )
        } catch (e: Exception) {
            Log.e("This is MidLandFcstRepository", "Error! : KmaRepositoryImpl", e)
            return null
        }
    }
}