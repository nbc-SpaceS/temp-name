package com.wannabeinseoul.seoulpublicservice.kma.midTemp

import android.util.Log

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
            Log.e("This is MidTempRepository", "Error! : TempRepositoryImpl", e)
            return null
        }
        val body = response.body() ?: return null
        return body.response?.body?.items?.item?.firstOrNull()
    }
}