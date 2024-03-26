package com.wannabeinseoul.seoulpublicservice.kma.midTemp

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

interface MidTempRepository {
    suspend fun getTemp(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<MidTemp>?
}

class MidTempRepositoryImpl(
    private val midTempApiService: MidTempApiService
) : MidTempRepository {
    override suspend fun getTemp(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<MidTemp>? {
        return try {
            midTempApiService.getTemp(
                numOfRows = numOfRows,
                pageNo = pageNo,
                dataType = dataType,
                regId = regId,
                tmFc = tmFc
            )
        } catch (e: IOException) {
            Log.e("This is MidTempRepository", "Network error! : TempRepositoryImpl", e)
            throw NetworkException("Network error!", e)
        } catch (e: HttpException) {
            Log.e("This is MidTempRepository", "HTTP request error! : TempRepositoryImpl", e)
            throw NetworkException("HTTP request error!", e)
        } catch (e: SocketTimeoutException) {
            Log.e("This is MidTempRepository", "Network timeout! : TempRepositoryImpl", e)
            throw NetworkException("Network timeout!", e)
        } catch (e: UnknownHostException) {
            Log.e("This is MidTempRepository", "Unknown host! : TempRepositoryImpl", e)
            throw NetworkException("Unknown host!", e)
        } catch (e: SSLHandshakeException) {
            Log.e("This is MidTempRepository", "SSL error! : TempRepositoryImpl", e)
            throw NetworkException("SSL error!", e)
        } catch (e: Exception) {
            Log.e("This is MidTempRepository", "Unexpected error! : TempRepositoryImpl", e)
            throw NetworkException("Unexpected error!", e)
        }
    }
}

class NetworkException(message: String, cause: Throwable) : Exception(message, cause)