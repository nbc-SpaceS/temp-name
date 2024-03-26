package com.wannabeinseoul.seoulpublicservice.kma.midLandFcst

import android.util.Log

interface KmaRepository {
    suspend fun getMidLandFcst(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Items
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
    ) = try
    {
        midLandFcstApiService.getMidLandFcst(
            numOfRows = numOfRows,
            pageNo = pageNo,
            dataType = dataType,
            regId = regId,
            tmFc = tmFc
        ).response.body.items
    } catch (e: Exception) {
        Log.e("This is MidLandFcstRepository", "Error! : KmaRepositoryImpl", e)
        KmaMidLandFcstDto.emptyMid().response.body.items
    }
}