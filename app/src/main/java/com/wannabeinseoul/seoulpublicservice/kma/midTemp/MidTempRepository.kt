package com.wannabeinseoul.seoulpublicservice.kma.midTemp

import android.util.Log

private const val TAG = "TempRepository"

interface TempRepository {
    suspend fun getTemp(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Item?
}

class TempRepositoryImpl(
    private val midTempApiService: MidTempApiService
) : TempRepository {
    override suspend fun getTemp(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Item? {
        val response = try {
            midTempApiService.getTemp(
                numOfRows = numOfRows,
                pageNo = pageNo,
                dataType = dataType,
                regId = regId,
                tmFc = tmFc
            )
        } catch (e: Throwable) {
            Log.e(
                TAG,
                "getTemp error. numOfRows:$numOfRows, pageNo:$pageNo" +
                        ", dataType:$dataType, regId:$regId, tmFc:$tmFc",
                e
            )
            return null
        }
        val body = response.body() ?: return null
            .apply { Log.w(TAG, "getMidLandFcst body() == null, response: $response") }
        return body.response?.body?.items?.item?.firstOrNull()
    }
}
