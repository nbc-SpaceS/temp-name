package com.wannabeinseoul.seoulpublicservice.seoul

import android.util.Log
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

private const val JJTAG = "jj-SeoulPublicRepository"

interface SeoulPublicRepository {
    /** 종합으로 첫 번째꺼 가져와서 전체 개수 확인하기 */
    suspend fun getTotalNum(): Int

    /** 종합으로 전체 병렬로 가져오기 */
    suspend fun getAllParallel(): List<Row>

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

    override suspend fun getTotalNum(): Int {
        Log.d(JJTAG, "getTotalNum getFirst 시간체크용")
        val response = try {
            seoulApiService.getFirst()
        } catch (e: Throwable) {
            Log.e(JJTAG, "getTotalNum getFirst error", e)
            return 0
        }
        val body = response.body() ?: return 0
            .apply { Log.w(JJTAG, "getTotalNum body == null, response: $response") }

        val logMsg =
            "total: ${body.tvYeyakCOllect.listTotalCount}, ${body.tvYeyakCOllect.result}\n" +
                    body.tvYeyakCOllect.rowList.firstOrNull().toString().take(255)
        Log.d(JJTAG, "getTotalNum getFirst 응답: $logMsg")
        return body.tvYeyakCOllect.listTotalCount.toInt()
    }

    override suspend fun getAllParallel(): List<Row> = coroutineScope {
        val batchSize = 192
        val total = getTotalNum()
        val batchTotal = (total + batchSize - 1) / batchSize
        val deferredList = List(batchTotal) { i ->
            async(Dispatchers.IO) {
                val from = i * batchSize + 1
                val to = (i + 1) * batchSize
                try {
                    val response = seoulApiService.getAllRange(from, to)
                    Log.d(
                        JJTAG,
                        "getAllParallel $from~$to, " + response.body().toString().take(255)
                    )
                    response.toRowList()
                } catch (e: Throwable) {
                    Log.e(JJTAG, "getAllParallel error $from~$to", e)
                    emptyList()
                }
            }
        }
        deferredList.awaitAll().flatten()
            .also {
                Log.d(
                    JJTAG, "getAllParallel return total ${it.size} in $total, " +
                            it.firstOrNull().toString().take(255)
                )
            }
    }

//    override suspend fun getAllParallel(batchSize: Int): List<Row> {
//        val total = getTotalNum()
//        val batchTotal = total / batchSize + 1
//        val responseList = List(batchTotal) { i ->
//            seoulApiService.getAllRange(i * batchSize, (i + 1) * batchSize)
//                .also { Log.d(JJTAG, "${it.body()}".take(255)) }
//        }
//        return responseList.flatMap { it.toRowList() }
//    }

    override suspend fun getAll1000(): List<Row> {
        val response = try {
            seoulApiService.getAll1000()
        } catch (e: Throwable) {
            Log.e(JJTAG, "getAll1000 error", e)
            return emptyList()
        }
        val body = response.body() ?: return emptyList<Row>()
            .also { Log.w(JJTAG, "getAll1000 response.body is null, response: $response") }
        val logMsg =
            "total: ${body.tvYeyakCOllect.listTotalCount}, ${body.tvYeyakCOllect.result}\n" +
                    body.tvYeyakCOllect.rowList.firstOrNull().toString().take(127)
        Log.d(JJTAG, "getAll1000 응답: $logMsg")
        return response.toRowList()
    }

    override suspend fun getAll2000(): List<Row> = coroutineScope {
        val deferred1 = async {
            val response = try {
                seoulApiService.getAllRange(1, 1000)
            } catch (e: Throwable) {
                Log.e(JJTAG, "getAll2000 getAllRange(1, 1000) error", e)
                return@async emptyList<Row>()
            }
            val body = response.body() ?: return@async emptyList<Row>()
                .also { Log.w(JJTAG, "getAll2000 1~1000 body == null, response: $response") }
            val logMsg =
                "total: ${body.tvYeyakCOllect.listTotalCount}, ${body.tvYeyakCOllect.result}\n" +
                        body.tvYeyakCOllect.rowList.firstOrNull().toString().take(127)
            Log.d(JJTAG, "getAll2000 1~1000 응답: $logMsg")
            response.toRowList()
        }
        val deferred2 = async {
            val response = try {
                seoulApiService.getAllRange(1001, 2000)
            } catch (e: Throwable) {
                Log.e(JJTAG, "getAll2000 getAllRange(1001, 2000) error", e)
                return@async emptyList()
            }
            val body = response.body() ?: return@async emptyList<Row>()
                .also { Log.w(JJTAG, "getAll2000 1001~2000 body == null, response: $response") }
            val logMsg =
                "total: ${body.tvYeyakCOllect.listTotalCount}, ${body.tvYeyakCOllect.result}\n" +
                        body.tvYeyakCOllect.rowList.firstOrNull().toString().take(127)
            Log.d(JJTAG, "getAll2000 1001~2000 응답: $logMsg")
            response.toRowList()
        }
        return@coroutineScope deferred1.await() + deferred2.await()
    }

    override suspend fun getDetail(svcid: String): DetailRow? {
        val response = try {
            seoulApiService.getDetail(svcid)
        } catch (e: Throwable) {
            Log.e(JJTAG, "getDetail", e)
            return null
        }
        val body = response.body() ?: return null
            .apply { Log.w(JJTAG, "getDetail body == null, response: $response") }
        try {
            val logMsg =
                "total: ${body.listPublicReservationDetail.listTotalCount}, ${body.listPublicReservationDetail.result}\n" +
                        body.listPublicReservationDetail.rowList.firstOrNull().toString()
                            .take(127)
            Log.d(JJTAG, "getDetail 응답: $logMsg")
        } catch (e: Throwable) {
            Log.w(JJTAG, "getDetail svcid: $svcid, response: $response", e)
        }
        return response.toDetailRow()
    }

    private fun Response<SeoulDto>.toRowList(): List<Row> =
        this.body()?.tvYeyakCOllect?.rowList ?: emptyList()

    private fun Response<SeoulDetailDto>.toDetailRow(): DetailRow? =
        this.body()?.listPublicReservationDetail?.rowList?.firstOrNull()

//    private fun convertResponseToItems(response: Response<SeoulDto>): List<Row> =
//        response.body()?.tvYeyakCOllect?.rowList ?: emptyList()
//
//    private fun convertDetailResponseToItem(response: Response<SeoulDetailDto>): DetailRow? =
//        response.body()?.listPublicReservationDetail?.rowList?.firstOrNull()

}
