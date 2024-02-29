package com.example.seoulpublicservice.seoul

import android.util.Log
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

interface SeoulPublicRepository {
    /** 종합으로 첫 천개 가져오기 */
    suspend fun getAll1000(): List<Row>

    /** 종합으로 2천개 가져오기 */
    suspend fun getAll2000(): List<Row>

    /** 서비스 id로 상세 정보 가져오기 */
    suspend fun getDetail(svcid: String): DetailRow?
}

class SeoulPublicRepositoryImpl(
    private val seoulApiService: SeoulApiService
) : SeoulPublicRepository {

    override suspend fun getAll1000(): List<Row> {
        val response = seoulApiService.getAll1000()
        val body = response.body()
        if (body == null) {
            Log.d(
                "jj-SeoulPublicRepositoryImpl",
                "getAll1000 body == null"
            )
        } else {
            val s =
                "total: ${body.tvYeyakCOllect.listTotalCount}, ${body.tvYeyakCOllect.result}\n" +
                        body.tvYeyakCOllect.rowList.firstOrNull().toString().take(127)
            Log.d(
                "jj-SeoulPublicRepositoryImpl",
                "getAll1000 응답: $s"
            )
        }
        return convertResponseToItems(response)
    }

    override suspend fun getAll2000(): List<Row> = coroutineScope {
        val deferred1 = async {
            val response = seoulApiService.getAllRange(1, 1000)
            val body = response.body()
            if (body == null) {
                Log.d(
                    "jj-SeoulPublicRepositoryImpl",
                    "getAll2000 1~1000 body == null"
                )
            } else {
                val s =
                    "total: ${body.tvYeyakCOllect.listTotalCount}, ${body.tvYeyakCOllect.result}\n" +
                            body.tvYeyakCOllect.rowList.firstOrNull().toString().take(127)
                Log.d(
                    "jj-SeoulPublicRepositoryImpl",
                    "getAll2000 1~1000 응답: $s"
                )
            }
            convertResponseToItems(response)
        }
        val deferred2 = async {
            val response = seoulApiService.getAllRange(1001, 2000)
            val body = response.body()
            if (body == null) {
                Log.d(
                    "jj-SeoulPublicRepositoryImpl",
                    "getAll2000 1001~2000 body == null"
                )
            } else {
                val s =
                    "total: ${body.tvYeyakCOllect.listTotalCount}, ${body.tvYeyakCOllect.result}\n" +
                            body.tvYeyakCOllect.rowList.firstOrNull().toString().take(127)
                Log.d(
                    "jj-SeoulPublicRepositoryImpl",
                    "getAll2000 1001~2000 응답: $s"
                )
            }
            convertResponseToItems(response)
        }
        return@coroutineScope deferred1.await() + deferred2.await()
    }

    override suspend fun getDetail(svcid: String): DetailRow? {
        val response = seoulApiService.getDetail(svcid)
        val body = response.body()
        if (body == null) {
            Log.d(
                "jj-SeoulPublicRepositoryImpl",
                "getDetail body == null"
            )
        } else {
            try {
                val s =
                    "total: ${body.listPublicReservationDetail.listTotalCount}, ${body.listPublicReservationDetail.result}\n" +
                            body.listPublicReservationDetail.rowList.firstOrNull().toString()
                                .take(127)
                Log.d(
                    "jj-SeoulPublicRepositoryImpl",
                    "getDetail 응답: $s"
                )
            } catch (e: Exception) {
                Log.d(
                    "jj-SeoulPublicRepositoryImpl",
                    "getDetail e: $e\n" +
                            "svcid: $svcid, response: $response"
                )
            }
        }
        return convertDetailResponseToItem(response)
    }

    private fun convertResponseToItems(response: Response<SeoulDto>): List<Row> =
        response.body()?.tvYeyakCOllect?.rowList ?: emptyList()

    private fun convertDetailResponseToItem(response: Response<SeoulDetailDto>): DetailRow? =
        response.body()?.listPublicReservationDetail?.rowList?.firstOrNull()

}
