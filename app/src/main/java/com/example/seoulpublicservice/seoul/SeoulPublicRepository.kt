package com.example.seoulpublicservice.seoul

import android.util.Log
import com.example.seoulpublicservice.util.trimUpTo
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import retrofit2.Response

interface SeoulPublicRepository {
    /** 종합으로 첫 천개 가져오기 */
    suspend fun getAll1000(): List<Row>

    /** 종합으로 2천개 가져오기 */
    suspend fun getAll2000(): List<Row>
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
                        body.tvYeyakCOllect.rowList.firstOrNull().toString().trimUpTo(127)
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
                    "getAll2000 천 천개 body == null"
                )
            } else {
                val s =
                    "total: ${body.tvYeyakCOllect.listTotalCount}, ${body.tvYeyakCOllect.result}\n" +
                            body.tvYeyakCOllect.rowList.firstOrNull().toString().trimUpTo(127)
                Log.d(
                    "jj-SeoulPublicRepositoryImpl",
                    "getAll2000 천 천개 응답: $s"
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
                    "getAllFirst2000 1001~2000 body == null"
                )
            } else {
                val s =
                    "total: ${body.tvYeyakCOllect.listTotalCount}, ${body.tvYeyakCOllect.result}\n" +
                            body.tvYeyakCOllect.rowList.firstOrNull().toString().trimUpTo(127)
                Log.d(
                    "jj-SeoulPublicRepositoryImpl",
                    "getAllFirst2000 1001~2000 응답: $s"
                )
            }
            convertResponseToItems(response)
        }
        return@coroutineScope deferred1.await() + deferred2.await()
    }

    private fun convertResponseToItems(response: Response<SeoulDto>): List<Row> =
        response.body()?.tvYeyakCOllect?.rowList ?: emptyList()

}
