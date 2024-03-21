package com.wannabeinseoul.seoulpublicservice.kma

import com.wannabeinseoul.seoulpublicservice.BuildConfig
import retrofit2.Response
interface KmaRepository {
    suspend fun getMidLandFcst(
        serviceKey: String,
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<KmaMidLandFcstDto>
}

class KmaRepositoryImpl(
    private val kmaApiService: KmaApiService
) : KmaRepository {
    override suspend fun getMidLandFcst(
        serviceKey: String,
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<KmaMidLandFcstDto> {
        return kmaApiService.getMidLandFcst(BuildConfig.KMA_API_KEY, numOfRows, pageNo, dataType, regId, tmFc)
    }
}