package com.wannabeinseoul.seoulpublicservice.pref

import android.content.Context
import android.content.SharedPreferences

interface RecommendPrefRepository {
    fun save(value: List<String>)
    fun load(): List<String>
}

class RecommendPrefRepositoryImpl(context: Context) : RecommendPrefRepository {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("recommend_prefs", Context.MODE_PRIVATE)
    private val KEY_RECOMMENDATIONS = "recommendations"

    override fun save(value: List<String>) {
        // Save logic here
    }
    override fun load(): List<String> {

        return sharedPreferences.getString(KEY_RECOMMENDATIONS, "")?.split(",") ?: emptyList()
    }
}
//    suspend fun getRecommendations(): List<RecommendMultiView> {
        // Perform API call here and return the data
        // Example:
////        val response = service.getRecommendations()
//        if (response.isSuccessful) {
//            // Handle successful response
//            return response.body() ?: emptyList()
//        } else {
//            // Handle error response
//            throw ApiException("Failed to fetch recommendations: ${response.message()}")
//        }
//    }
//}
//


//    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("recommend_shared_prefs", Context.MODE_PRIVATE)
//
//    override fun save(key: String, value: String) {
//        sharedPreferences.edit().putString(key, value).apply()
//    }
//
//    override fun load(key: String): String {
//        return sharedPreferences.getString(key, "") ?: ""
//    }
//    fun convertToRow(reservations: List<ReservationEntity>): List<Row> {
//        return reservations.map { reservation ->
//            Row(
//                gubun = reservation.GUBUN,
//                placenm = reservation.PLACENM,
//                payatnm = reservation.PAYATNM,
//                svcstatnm = reservation.SVCSTATNM,
//                areanm = reservation.AREANM,
//                imgurl = reservation.IMGURL,
//                maxclassnm = "",
//                minclassnm = "",
//                rcptbgndt = "",
//                rcptenddt = "",
//                revstdday = "".toLong(),
//                revstddaynm = "",
//                svcid = "",
//                svcnm = "",
//                svcopnbgndt = "",
//                svcopnenddt = "",
//                svcurl = "",
//                telno = "",
//                usetgtinfo = "",
//                vMax = "",
//                vMin = "",
//                x = "",
//                y = "",
//                dtlcont = "",
//                div = "",
//                service = ""
//            )
//        }
//    }
//}