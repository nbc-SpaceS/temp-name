package com.wannabeinseoul.seoulpublicservice.kma.midLandFcst

import android.util.Log
import com.wannabeinseoul.seoulpublicservice.weather.WeatherData

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
//        val body = response.body() ?: return null.apply { Log.w(TAG, "getMidLandFcst body() == null, response: $response") }
//        val item = body.response?.body?.items?.itemList?.firstOrNull()
//        return item
        val body = response.body()
            ?: return if (WeatherData.getMid() == null) {
                null.apply {
                    Log.w(
                        TAG,
                        "getMidLandFcst return if(WeatherData.getMid() == null), response: $response"
                    )
                }
            } else {
                WeatherData.getMid()
            }
        val item = body.response?.body?.items?.itemList?.firstOrNull()
        if(item != null) WeatherData.saveMid(item)
        return item
    }
}
