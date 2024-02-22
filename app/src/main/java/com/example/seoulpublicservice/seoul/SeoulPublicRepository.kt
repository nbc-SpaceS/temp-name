package com.example.seoulpublicservice.seoul

import android.util.Log
import retrofit2.Response

interface SeoulPublicRepository {
    /** 종합으로 첫 천개 가져오기 */
    suspend fun getAllFirst1000(): List<Row>
}

class SeoulPublicRepositoryImpl(
    private val seoulApiService: SeoulApiService
) : SeoulPublicRepository {
    override suspend fun getAllFirst1000(): List<Row> {
        val response = seoulApiService.getAllFirst1000()
        Log.d(
            "jj-SeoulPublicRepositoryImpl",
            "listTotalCount: ${response.body()?.tvYeyakCOllect?.listTotalCount}, " +
                    "result: ${response.body()?.tvYeyakCOllect?.result}"
        )
        val s = response.body()?.tvYeyakCOllect?.rowList?.firstOrNull().toString().let {
            if (it.length <= 64) it else it.substring(0, 64)
        }
        Log.d("jj-SeoulPublicRepositoryImpl", "first row length 64: $s")
        return convertResponseToItems(response)
    }

    private fun convertResponseToItems(response: Response<SeoulDto>): List<Row> =
        response.body()?.tvYeyakCOllect?.rowList ?: emptyList()
}
