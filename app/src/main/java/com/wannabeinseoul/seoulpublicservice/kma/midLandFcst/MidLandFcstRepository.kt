package com.wannabeinseoul.seoulpublicservice.kma.midLandFcst

import android.util.Log
import retrofit2.HttpException
import retrofit2.Response
import java.io.IOException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

interface KmaRepository {
    suspend fun getMidLandFcst(
        numOfRows: Int,
        pageNo: Int,
        dataType: String,
        regId: String,
        tmFc: String
    ): Response<KmaMidLandFcstDto>?
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
    ): Response<KmaMidLandFcstDto>? {
        return try {
            midLandFcstApiService.getMidLandFcst(
                numOfRows = numOfRows,
                pageNo = pageNo,
                dataType = dataType,
                regId = regId,
                tmFc = tmFc
            )
        } catch (e: IOException) {
            Log.e("This is MidLandFcstRepository", "Network error! : KmaRepositoryImpl", e)
            throw NetworkException("Network error!", e)
        } catch (e: HttpException) {
            Log.e("This is MidLandFcstRepository", "HTTP request error! : KmaRepositoryImpl", e)
            throw NetworkException("HTTP request error!", e)
        } catch (e: SocketTimeoutException) {
            Log.e("This is MidLandFcstRepository", "Network timeout! : KmaRepositoryImpl", e)
            throw NetworkException("Network timeout!", e)
        } catch (e: UnknownHostException) {
            Log.e("This is MidLandFcstRepository", "Unknown host! : KmaRepositoryImpl", e)
            throw NetworkException("Unknown host!", e)
        } catch (e: SSLHandshakeException) {
            Log.e("This is MidLandFcstRepository", "SSL error! : KmaRepositoryImpl", e)
            throw NetworkException("SSL error!", e)
        } catch (e: Exception) {
            Log.e("This is MidLandFcstRepository", "Unexpected error! : KmaRepositoryImpl", e)
            throw NetworkException("Unexpected error!", e)
        }
    }
}

class NetworkException(message: String, cause: Throwable) : Exception(message, cause)