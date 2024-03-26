package com.wannabeinseoul.seoulpublicservice.kma.midLandFcst

import android.util.Log

private const val TAG = "KmaRepository"

interface KmaRepository {
    suspend fun getMidLandFcst(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Item?
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
    ): Item? {
        val response = try {
            midLandFcstApiService.getMidLandFcst(
                numOfRows = numOfRows,
                pageNo = pageNo,
                dataType = dataType,
                regId = regId,
                tmFc = tmFc
            )
        } catch (e: Throwable) {
            Log.e(
                TAG,
                "getMidLandFcst error. numOfRows:$numOfRows, pageNo:$pageNo" +
                        ", dataType:$dataType, regId:$regId, tmFc:$tmFc",
                e
            )
            return null
        }
        val body = response.body() ?: return null
            .apply { Log.w(TAG, "getMidLandFcst body() == null, response: $response") }
        return body.response?.body?.items?.itemList?.firstOrNull()
    }
}
