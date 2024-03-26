package com.wannabeinseoul.seoulpublicservice.kma.midTemp

import android.util.Log

interface TempRepository {
    suspend fun getTemp(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Items
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
    ) = try
    {
        midTempApiService.getTemp(
            numOfRows = numOfRows,
            pageNo = pageNo,
            dataType = dataType,
            regId = regId,
            tmFc = tmFc
        ).response.body.items
    } catch (e: Exception) {
        Log.e("This is MidTempRepository", "Error! : TempRepositoryImpl", e)
        TemperatureDTO.emptyTemp().response.body.items
    }
}