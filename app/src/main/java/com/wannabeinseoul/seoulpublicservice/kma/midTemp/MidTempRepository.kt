package com.wannabeinseoul.seoulpublicservice.kma.midTemp

import retrofit2.Response

interface TempRepository {
    suspend fun getTemp(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<TemperatureDTO>
}

class TempRepositoryImpl (
    private val midTempApiService: MidTempApiService
): TempRepository {
    override suspend fun getTemp(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<TemperatureDTO> {
        return midTempApiService.getTemp(numOfRows = numOfRows, pageNo = pageNo, dataType = dataType, regId = regId, tmFc = tmFc)
    }
}