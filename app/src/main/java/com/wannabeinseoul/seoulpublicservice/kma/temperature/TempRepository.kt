package com.wannabeinseoul.seoulpublicservice.kma.temperature

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
    private val tempApiService: TempApiService
): TempRepository {
    override suspend fun getTemp(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<TemperatureDTO> {
        return tempApiService.getTemp(numOfRows, pageNo, dataType, regId, tmFc)
    }
}